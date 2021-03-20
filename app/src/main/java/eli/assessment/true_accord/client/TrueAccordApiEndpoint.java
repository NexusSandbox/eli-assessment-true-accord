package eli.assessment.true_accord.client;


import java.net.MalformedURLException;
import java.net.URL;

public enum TrueAccordApiEndpoint {
    DEBTS("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts"),
    PAYMENT_PLANS("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans"),
    PAYMENTS("https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments");

    final public URL serviceURL;

    TrueAccordApiEndpoint(final String url) {
        try {
            serviceURL = new URL(url);
        } catch(MalformedURLException e) {
            throw new RuntimeException(String.format("Invalid URL format [%s]", url), e);
        }
    }
}
