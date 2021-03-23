package eli.assessment.true_accord.client;

import eli.assessment.true_accord.Debt;
import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;
import eli.assessment.true_accord.api.InstallmentFrequency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Performs basic validation for ensuring the {@link Debt} logic meets requirements
 */
public class DebtTest {
    /**
     * Validates {@link Debt#isInPaymentPlan} for {@link Debt} with no associated {@link APIPaymentPlan}
     * <p/>
     * Expected: {@link Debt#isInPaymentPlan} -> false
     */
    @Test
    public void validateDebtWithNoPaymentPlanIsNotInRepayment() {
        final APIDebt apiDebt  = new APIDebt(0, 1);
        final Debt    testDebt = new Debt(apiDebt, null, List.of());
        Assertions.assertFalse(testDebt.isInPaymentPlan,
                               "Expected a Debt not associated to a paymentPlan to NOT be in repaymentplan");
    }

    /**
     * Validates {@link Debt#isInPaymentPlan} for {@link Debt} with zero balance in associated {@link APIPaymentPlan}
     * <p/>
     * Expected: {@link Debt#isInPaymentPlan} -> false
     */
    @Test
    public void validateDebtWithZeroBalanceIsNotInRepayment() {
        final APIDebt apiDebt = new APIDebt(0, 1);
        final APIPaymentPlan apiPaymentPlan = new APIPaymentPlan(0,
                                                                 0,
                                                                 0f,
                                                                 InstallmentFrequency.WEEKLY,
                                                                 1f,
                                                                 "2021-01-01");
        final Debt testDebt = new Debt(apiDebt, apiPaymentPlan, List.of());
        Assertions.assertFalse(testDebt.isInPaymentPlan,
                               "Expected a Debt with a $0 balance to NOT be in repayment");
    }

    /**
     * Validates {@link Debt#isInPaymentPlan} for {@link Debt} with non-zero balance in associated {@link APIPaymentPlan}
     * <p/>
     * Expected: {@link Debt#isInPaymentPlan} -> true
     */
    @Test
    public void validateDebtWithNonZeroBalanceIsInRepayment() {
        final APIDebt apiDebt = new APIDebt(0, 1);
        final APIPaymentPlan apiPaymentPlan = new APIPaymentPlan(0,
                                                                 0,
                                                                 1f,
                                                                 InstallmentFrequency.WEEKLY,
                                                                 1f,
                                                                 "2021-01-01");
        final Debt testDebt = new Debt(apiDebt, apiPaymentPlan, List.of());
        Assertions.assertTrue(testDebt.isInPaymentPlan,
                              "Expected a Debt with a non-zero balance to be in repayment");
    }

    /**
     * Validates {@link Debt#remainingAmount} for {@link Debt} with no associated {@link APIPaymentPlan}
     * <p/>
     * Expected: {@link Debt#remainingAmount} -> {@link APIDebt#amount}
     */
    @Test
    public void validateDebtWithNoPaymentPlanRemainingAmountMatchesOriginalDebt() {
        final APIDebt apiDebt  = new APIDebt(0, 1f);
        final Debt    testDebt = new Debt(apiDebt, null, List.of());
        Assertions.assertEquals(1,
                                testDebt.remainingAmount,
                                "Expected a debt's remaining amount with no payment plan to match the original debt");
    }

    /**
     * Validates {@link Debt#remainingAmount} for {@link Debt} with associated {@link APIPaymentPlan} and existing {@link APIPayment}s
     * <p/>
     * Expected: {@link Debt#remainingAmount} -> {@link APIPaymentPlan#amountToPay} - (All {@link APIPayment} objects)
     */
    @Test
    public void validateDebtWithRemainingAmountIsReducedByPayments() {
        final APIDebt apiDebt = new APIDebt(0, 100);
        final APIPaymentPlan apiPaymentPlan = new APIPaymentPlan(0,
                                                                 0,
                                                                 100f,
                                                                 InstallmentFrequency.WEEKLY,
                                                                 10f,
                                                                 "2021-01-01");
        final List<APIPayment> apiPayments = List.of(
                new APIPayment(0, 10f, "2021-02-01"),
                new APIPayment(0, 5f, "2021-03-01")
        );
        final Debt testDebt = new Debt(apiDebt, apiPaymentPlan, apiPayments);
        Assertions.assertEquals(85f, testDebt.remainingAmount,
                                "Expected a debt's remaining amount to be reduced by the provided payments");
    }

    /**
     * Validates {@link Debt#remainingAmount} for {@link Debt} with associated {@link APIPaymentPlan} and existing {@link APIPayment}s that exceed amount due
     * <p/>
     * Expected: {@link Debt#remainingAmount} -> 0
     */
    @Test
    public void validateDebtWithRemainingAmountIsZeroIfOverpayment() {
        final APIDebt apiDebt = new APIDebt(0, 100);
        final APIPaymentPlan apiPaymentPlan = new APIPaymentPlan(0,
                                                                 0,
                                                                 100f,
                                                                 InstallmentFrequency.WEEKLY,
                                                                 10f,
                                                                 "2021-01-01");
        final List<APIPayment> apiPayments = List.of(
                new APIPayment(0, 100f, "2021-02-01"),
                new APIPayment(0, 50f, "2021-03-01")
        );
        final Debt testDebt = new Debt(apiDebt, apiPaymentPlan, apiPayments);
        Assertions.assertEquals(0f, testDebt.remainingAmount,
                                "Expected a debt's remaining amount to be 0, if payments exceed payment amount");
    }

    /**
     * Validates {@link Debt#nextPaymentDueDate} for {@link Debt} with no associated {@link APIPaymentPlan}
     * <p/>
     * Expected: {@link Debt#nextPaymentDueDate} -> null
     */
    @Test
    public void validateDebtWithNoPaymentPlanDoesNotHaveDueDate() {
        final APIDebt apiDebt  = new APIDebt(0, 100);
        final Debt    testDebt = new Debt(apiDebt, null, List.of());
        Assertions.assertNull(testDebt.nextPaymentDueDate,
                              "Expected a debt's due date to be null if not on payment plan");
    }

    /**
     * Validates {@link Debt#nextPaymentDueDate} for {@link Debt} with associated {@link APIPaymentPlan} with {@link InstallmentFrequency#WEEKLY} installments in recent past
     * <p/>
     * Expected: {@link Debt#nextPaymentDueDate} -> tomorrow's date
     */
    @Test
    public void validateDebtWithPaymentPlanHasExpectedWeeklyDueDate() {
        final APIDebt   apiDebt   = new APIDebt(0, 100);
        final LocalDate startDate = LocalDate.now().minusDays(6);
        final APIPaymentPlan apiPaymentPlan = new APIPaymentPlan(0,
                                                                 0,
                                                                 90f,
                                                                 InstallmentFrequency.WEEKLY,
                                                                 10f,
                                                                 startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        final Debt      testDebt    = new Debt(apiDebt, apiPaymentPlan, List.of());
        final LocalDate nextDueDate = LocalDate.now().plusDays(1);
        Assertions.assertEquals(nextDueDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                testDebt.nextPaymentDueDate,
                                "Expected a debt with a recent starting repayment date (6 days ago with weekly installments) due date to be tomorrow");
    }

    /**
     * Validates {@link Debt#nextPaymentDueDate} for {@link Debt} with associated {@link APIPaymentPlan} with {@link InstallmentFrequency#BI_WEEKLY} installments in recent past
     * <p/>
     * Expected: {@link Debt#nextPaymentDueDate} -> tomorrow's date
     */
    @Test
    public void validateDebtWithPaymentPlanHasExpectedBiWeeklyDueDate() {
        final APIDebt   apiDebt   = new APIDebt(0, 100);
        final LocalDate startDate = LocalDate.now().minusDays(13);
        final APIPaymentPlan apiPaymentPlan = new APIPaymentPlan(0,
                                                                 0,
                                                                 90f,
                                                                 InstallmentFrequency.BI_WEEKLY,
                                                                 10f,
                                                                 startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        final Debt      testDebt    = new Debt(apiDebt, apiPaymentPlan, List.of());
        final LocalDate nextDueDate = LocalDate.now().plusDays(1);
        Assertions.assertEquals(nextDueDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                testDebt.nextPaymentDueDate,
                                "Expected a debt with a recent starting repayment date (6 days ago with weekly installments) due date to be tomorrow");
    }
}
