package eli.assessment.true_accord.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;

import java.io.IOException;
import java.util.List;

public class TrueAccordClient {
    final private static ObjectMapper mapper = new ObjectMapper();

    public static List<APIDebt> retrieveDebts() throws IOException {
        List<APIDebt> response = mapper.readValue(TrueAccordApiEndpoint.DEBTS.serviceURL, new TypeReference<>() {
        });
        System.out.printf("Debts: %s%n", mapper.writeValueAsString(response));
        return response;
    }

    public static List<APIPaymentPlan> retrievePaymentPlans() throws IOException {
        List<APIPaymentPlan> response = mapper.readValue(TrueAccordApiEndpoint.PAYMENT_PLANS.serviceURL,
                                                         new TypeReference<>() {
                                                         });
        System.out.printf("Payment Plans: %s%n", mapper.writeValueAsString(response));
        return response;
    }

    public static List<APIPayment> retrievePayments() throws IOException {
        List<APIPayment> response = mapper.readValue(TrueAccordApiEndpoint.PAYMENTS.serviceURL, new TypeReference<>() {
        });
        System.out.printf("Payments: %s%n", mapper.writeValueAsString(response));
        return response;
    }
}
