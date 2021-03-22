package eli.assessment.true_accord.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;

import java.io.IOException;
import java.util.List;

public class TrueAccordClient {
    final private static ObjectMapper mapper = new ObjectMapper();

    public static List<APIDebt> retrieveDebts() {
        try {
            return mapper.readValue(TrueAccordApiEndpoint.DEBTS.serviceURL,
                                    new TypeReference<>() {
                                    });
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON APIDebt objects", e);
        } catch(IOException e) {
            throw new RuntimeException("Unable to retrieve APIDebt objects from external API", e);
        }
    }

    public static List<APIPaymentPlan> retrievePaymentPlans() {
        try {
            return mapper.readValue(TrueAccordApiEndpoint.PAYMENT_PLANS.serviceURL,
                                    new TypeReference<>() {
                                    });
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON APIPaymentPlan objects", e);
        } catch(IOException e) {
            throw new RuntimeException("Unable to retrieve APIPaymentPlan objects from external API", e);
        }
    }

    public static List<APIPayment> retrievePayments() {
        try {
            return mapper.readValue(TrueAccordApiEndpoint.PAYMENTS.serviceURL,
                                    new TypeReference<>() {
                                    });
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON APIPayment objects", e);
        } catch(IOException e) {
            throw new RuntimeException("Unable to retrieve APIPayment objects from external API", e);
        }
    }
}
