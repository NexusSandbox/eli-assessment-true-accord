package eli.assessment.true_accord.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TrueAccordClientTest {
    @Test
    void healthCheckRetrieveDebts() throws IOException {
        TrueAccordClient.retrieveDebts();
    }

    @Test
    void healthCheckRetrievePayments() throws IOException {
        TrueAccordClient.retrievePayments();
    }

    @Test
    void healthCheckRetrievePaymentPlans() throws IOException {
        TrueAccordClient.retrievePaymentPlans();
    }
}
