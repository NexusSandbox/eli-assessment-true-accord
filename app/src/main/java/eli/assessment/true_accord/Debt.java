package eli.assessment.true_accord;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Debt {
    public               APIDebt          debt;
    /**
     * Indicates if debt has an associated payment plan and is not currently paid-off
     */
    @JsonProperty("is_in_payment_plan")
    public               boolean          isInPaymentPlan;
    /**
     * Amount (in USD) remaining on debt
     */
    @JsonProperty("remaining_amount")
    public               float            remainingAmount;
    /**
     * ISO 8601 UTC date of when the next payment is due or null if there is no payment plan or if the debt has been paid-off
     */
    @JsonProperty("next_payment_due_date")
    public               String           nextPaymentDueDate;
    @JsonIgnore
    public               APIPaymentPlan   paymentPlan;
    @JsonIgnore
    public               List<APIPayment> payments;
    @JsonIgnore
    final private        ObjectMapper     mapper = new ObjectMapper();
    @JsonIgnore
    final private static FluentLogger     logger = FluentLogger.forEnclosingClass();

    public Debt(APIDebt debt, APIPaymentPlan paymentPlan, List<APIPayment> payments) {
        this.debt          = debt;
        this.paymentPlan   = paymentPlan;
        this.payments      = payments;
        isInPaymentPlan    = checkIfIsPaymentPlan();
        remainingAmount    = calculateRemainingAmount();
        nextPaymentDueDate = calculateNextPaymentDueDate();
    }

    private boolean checkIfIsPaymentPlan() {
        return paymentPlan != null && paymentPlan.amountToPay > 0 && debt.amount > 0;
    }

    private float calculateRemainingAmount() {
        if(paymentPlan == null) {
            logger.atInfo().log("Debt <id=%d> has no payment plan. Original debt amount due: <amount=%f>.",
                                debt.id,
                                debt.amount);
            return debt.amount;
        } else if(!checkIfIsPaymentPlan()) {
            logger.atInfo().log("Debt <id=%d> is not currently in repayment. No remaining amount due.", debt.id);
            return 0;
        }
        final float remainingAmount = paymentPlan.amountToPay
                                      - payments.stream()
                                                .reduce(0F,
                                                        (Float subTotal, APIPayment payment) ->
                                                                subTotal + payment.amount,
                                                        Float::sum);
        logger.atInfo().log(
                "Debt <id=%d> is currently in repayment. Unadjusted calculated remaining amount due: <amount=%f>",
                debt.id,
                remainingAmount);
        return remainingAmount > 0 ? remainingAmount : 0;
    }

    private String calculateNextPaymentDueDate() {
        if(!checkIfIsPaymentPlan()) {
            logger.atInfo().log("Debt <id=%d> is not currently in repayment. Next due date is not applicable.",
                                debt.id);
            return null;
        }
        final LocalDate startDate      = LocalDate.parse(paymentPlan.startDate);
        final LocalDate currentDate    = LocalDate.now();
        final long      daysSinceStart = ChronoUnit.DAYS.between(startDate, currentDate);
        final int       interval       = paymentPlan.installmentFrequency.interval;
        final String nextPaymentDueDate = currentDate.plus(interval - daysSinceStart % interval, ChronoUnit.DAYS)
                                                     .format(DateTimeFormatter.ISO_LOCAL_DATE);
        logger.atInfo().log(
                "Debt <id=%d> is currently in repayment with <startDate=%s; currentDate=%s; daysBetween=%d; interval=%s days>. Calculated next due date: <nextDueDate=%s>",
                debt.id,
                startDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                daysSinceStart,
                interval,
                nextPaymentDueDate);
        return nextPaymentDueDate;
    }

    /**
     * @param debts        List of {@link APIDebt} objects corresponding to a client's debt
     * @param paymentPlans List of {@link APIPaymentPlan} objects corresponding to a specific debt
     * @param payments     List of {@link APIPayment} objects corresponding to a specific payment plan
     *
     * @return List of {@link Debt} objects with corresponding {@link APIDebt}, {@link APIPaymentPlan}, and {@link APIPayment} objects
     */
    public static List<Debt> of(List<APIDebt> debts, List<APIPaymentPlan> paymentPlans, List<APIPayment> payments) {
        return debts.stream().map(apiDebt -> {
            final Optional<APIPaymentPlan> paymentPlan = paymentPlans.stream()
                                                                     .filter(apiPaymentPlan -> apiPaymentPlan.debtId
                                                                                               == apiDebt.id)
                                                                     .findFirst();
            if(paymentPlan.isEmpty()) {
                logger.atInfo().log("Debt <id=%d> does not have any associated payment plans.", apiDebt.id);
                return new Debt(apiDebt, null, List.of());
            }
            final APIPaymentPlan currentPaymentPlan = paymentPlan.get();
            logger.atInfo().log("Debt <id=%d> associating to PaymentPlan <id=%d>.", apiDebt.id, currentPaymentPlan.id);
            final List<APIPayment> currentPayments = payments.stream()
                                                             .filter(apiPayment -> apiPayment.paymentPlanId
                                                                                   == currentPaymentPlan.id)
                                                             .collect(Collectors.toUnmodifiableList());
            return new Debt(apiDebt, currentPaymentPlan, currentPayments);
        }).collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return This {@link Debt} as a JSON formatted string
     */
    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to render Debt object as JSON", e);
        }
    }
}
