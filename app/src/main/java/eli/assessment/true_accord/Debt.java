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
    @JsonProperty("is_in_payment_plan")
    public               boolean          isInPaymentPlan;
    @JsonProperty("remaining_amount")
    public               float            remainingAmount;
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
            return debt.amount;
        } else if(!checkIfIsPaymentPlan()) {
            return 0;
        }
        final float remainingAmount = paymentPlan.amountToPay
                                      - payments.stream()
                                                .reduce(0F,
                                                        (Float subTotal, APIPayment payment) ->
                                                                subTotal + payment.amount,
                                                        Float::sum);
        return remainingAmount > 0 ? remainingAmount : 0;
    }

    private String calculateNextPaymentDueDate() {
        if(!checkIfIsPaymentPlan()) {
            return null;
        }
        final LocalDate startDate      = LocalDate.parse(paymentPlan.startDate);
        final LocalDate currentDate    = LocalDate.now();
        final long      daysSinceStart = ChronoUnit.DAYS.between(startDate, currentDate);
        final int       interval       = paymentPlan.installmentFrequency.interval;
        return currentDate.plus(interval - daysSinceStart % interval, ChronoUnit.DAYS)
                          .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static List<Debt> of(List<APIDebt> debts, List<APIPaymentPlan> paymentPlans, List<APIPayment> payments) {
        return debts.stream().map(apiDebt -> {
            final Optional<APIPaymentPlan> paymentPlan = paymentPlans.stream()
                                                                     .filter(apiPaymentPlan -> apiPaymentPlan.debtId
                                                                                               == apiDebt.id)
                                                                     .findFirst();
            if(paymentPlan.isEmpty()) {
                return new Debt(apiDebt, null, List.of());
            }
            final APIPaymentPlan currentPaymentPlan = paymentPlan.get();
            final List<APIPayment> currentPayments = payments.stream()
                                                             .filter(apiPayment -> apiPayment.paymentPlanId
                                                                                   == currentPaymentPlan.id)
                                                             .collect(Collectors.toUnmodifiableList());
            return new Debt(apiDebt, currentPaymentPlan, currentPayments);
        }).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to render Debt object as JSON", e);
        }
    }
}
