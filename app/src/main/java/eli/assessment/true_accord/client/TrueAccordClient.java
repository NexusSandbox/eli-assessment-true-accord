package eli.assessment.true_accord.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;

import java.io.IOException;
import java.util.List;

public class TrueAccordClient {
    final private static FluentLogger logger = FluentLogger.forEnclosingClass();
    final private static ObjectMapper mapper = new ObjectMapper();

    public static List<APIDebt> retrieveDebts() {
        try {
            logger.atInfo().log("Retrieving all debt objects from API...");
            final List<APIDebt> apiDebts = mapper.readValue(TrueAccordApiEndpoint.DEBTS.serviceURL,
                                                            new TypeReference<>() {
                                                            });
            logger.atInfo().log("...Debt objects retrieved.");
            return apiDebts;
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON APIDebt objects", e);
        } catch(IOException e) {
            throw new RuntimeException("Unable to retrieve APIDebt objects from external API", e);
        }
    }

    public static List<APIPaymentPlan> retrievePaymentPlans() {
        try {
            logger.atInfo().log("Retrieving all payment plan objects from API...");
            final List<APIPaymentPlan> apiPaymentPlans = mapper.readValue(TrueAccordApiEndpoint.PAYMENT_PLANS.serviceURL,
                                                                          new TypeReference<>() {
                                                                          });
            logger.atInfo().log("...Payment Plan objects retrieved.");
            return apiPaymentPlans;
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON APIPaymentPlan objects", e);
        } catch(IOException e) {
            throw new RuntimeException("Unable to retrieve APIPaymentPlan objects from external API", e);
        }
    }

    public static List<APIPayment> retrievePayments() {
        try {
            logger.atInfo().log("Retrieving all payment objects from API...");
            final List<APIPayment> apiPayments = mapper.readValue(TrueAccordApiEndpoint.PAYMENTS.serviceURL,
                                                                  new TypeReference<>() {
                                                                  });
            logger.atInfo().log("...Payment objects retrieved.");
            return apiPayments;
        } catch(JsonProcessingException e) {
            throw new RuntimeException("Unable to parse JSON APIPayment objects", e);
        } catch(IOException e) {
            throw new RuntimeException("Unable to retrieve APIPayment objects from external API", e);
        }
    }
}
