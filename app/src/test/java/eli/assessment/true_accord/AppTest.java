package eli.assessment.true_accord;

import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;
import eli.assessment.true_accord.api.InstallmentFrequency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Performs basic validation for ensuring generated {@link Debt} objects are correctly formatted
 */
class AppTest {
    /**
     * Validates {@link App#generateDebtsJson(List)} for a sample list of debts
     * <p/>
     * Expected: List of 2 Strings of properly formatted JSON
     */
    @Test
    void validateGeneratesDebtsJson() {
        final APIDebt apiDebt1 = new APIDebt(0, 100);
        final APIPaymentPlan apiPaymentPlan1 = new APIPaymentPlan(0,
                                                                  0,
                                                                  100f,
                                                                  InstallmentFrequency.WEEKLY,
                                                                  10f,
                                                                  "2021-01-01");
        final List<APIPayment> apiPayments1 = List.of(
                new APIPayment(0, 100f, "2021-02-01"),
                new APIPayment(0, 50f, "2021-03-01")
        );
        final Debt    testDebt1 = new Debt(apiDebt1, apiPaymentPlan1, apiPayments1);
        final APIDebt apiDebt2  = new APIDebt(1, 200);
        final APIPaymentPlan apiPaymentPlan2 = new APIPaymentPlan(1,
                                                                  1,
                                                                  151f,
                                                                  InstallmentFrequency.WEEKLY,
                                                                  10f,
                                                                  "2021-03-01");
        final List<APIPayment> apiPayments2 = List.of(
                new APIPayment(1, 100f, "2021-03-01"),
                new APIPayment(1, 50f, "2021-03-15")
        );
        final Debt         testDebt2  = new Debt(apiDebt2, apiPaymentPlan2, apiPayments2);
        final List<String> testResult = App.generateDebtsJson(List.of(testDebt1, testDebt2));
        Assertions.assertIterableEquals(List.of(
                "{\"debt\":{\"id\":0,\"amount\":100.0},\"is_in_payment_plan\":true,\"remaining_amount\":0.0,\"next_payment_due_date\":\"2021-03-26\"}",
                "{\"debt\":{\"id\":1,\"amount\":200.0},\"is_in_payment_plan\":true,\"remaining_amount\":1.0,\"next_payment_due_date\":\"2021-03-29\"}"),
                                        testResult,
                                        "Unexpected generated JSON outputs");
    }
}
