package eli.assessment.true_accord.client;

import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TrueAccordClientTest {
    @Test
    void healthCheckRetrieveDebts() {
        final List<APIDebt> results = TrueAccordClient.retrieveDebts();
        Assertions.assertTrue(results.size() > 0, "Unable to retrieve debts from API");
    }

    @Test
    void healthCheckRetrievePayments() {
        final List<APIPayment> results = TrueAccordClient.retrievePayments();
        Assertions.assertTrue(results.size() > 0, "Unable to retrieve payments from API");
    }

    @Test
    void healthCheckRetrievePaymentPlans() {
        final List<APIPaymentPlan> results = TrueAccordClient.retrievePaymentPlans();
        Assertions.assertTrue(results.size() > 0, "Unable to retrieve payment plans from API");
    }
}
