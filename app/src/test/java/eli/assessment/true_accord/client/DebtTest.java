package eli.assessment.true_accord.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class DebtTest {
    final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void validateDebtWithNoPaymentPlanIsNotInRepayment() {
        final APIDebt apiDebt  = new APIDebt(0, 1);
        final Debt    testDebt = new Debt(apiDebt, null, List.of());
        Assertions.assertFalse(testDebt.isInPaymentPlan,
                               "Expected a Debt not associated to a paymentPlan to NOT be in repaymentplan");
    }

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

    @Test
    public void validateDebtWithNoPaymentPlanRemainingAmountMatchesOriginalDebt() {
        final APIDebt apiDebt  = new APIDebt(0, 1f);
        final Debt    testDebt = new Debt(apiDebt, null, List.of());
        Assertions.assertEquals(1,
                                testDebt.remainingAmount,
                                "Expected a debt's remaining amount with no payment plan to match the original debt");
    }

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

    @Test
    public void validateDebtWithNoPaymentPlanDoesNotHaveDueDate() {
        final APIDebt apiDebt  = new APIDebt(0, 100);
        final Debt    testDebt = new Debt(apiDebt, null, List.of());
        Assertions.assertNull(testDebt.nextPaymentDueDate,
                              "Expected a debt's due date to be null if not on payment plan");
    }

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
