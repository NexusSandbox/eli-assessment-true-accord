package eli.assessment.true_accord.client;

import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Performs health checks on TrueAccord external API
 */
public class TrueAccordClientTest {
    /**
     * Health Check for Debts API Endpoint
     * <p/>
     * Expected: Non-empty List of {@link APIDebt} objects
     */
    @Test
    void healthCheckRetrieveDebts() {
        final List<APIDebt> results = TrueAccordClient.retrieveDebts();
        Assertions.assertFalse(results.isEmpty(), "Unable to retrieve debts from API");
    }

    /**
     * Health Check for Payment API Endpoint
     * <p/>
     * Expected: Non-empty List of {@link APIPayment} objects
     */
    @Test
    void healthCheckRetrievePayments() {
        final List<APIPayment> results = TrueAccordClient.retrievePayments();
        Assertions.assertFalse(results.isEmpty(), "Unable to retrieve payments from API");
    }

    /**
     * Health Check for Payment Plan API Endpoint
     * <p/>
     * Expected: Non-empty List of {@link APIPaymentPlan} objects
     */
    @Test
    void healthCheckRetrievePaymentPlans() {
        final List<APIPaymentPlan> results = TrueAccordClient.retrievePaymentPlans();
        Assertions.assertFalse(results.isEmpty(), "Unable to retrieve payment plans from API");
    }
}
