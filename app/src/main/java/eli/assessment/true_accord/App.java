package eli.assessment.true_accord;

import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;
import eli.assessment.true_accord.client.TrueAccordClient;

import java.util.List;

public class App {
    final static List<APIDebt>        apiDebts        = TrueAccordClient.retrieveDebts();
    final static List<APIPaymentPlan> apiPaymentPlans = TrueAccordClient.retrievePaymentPlans();
    final static List<APIPayment>     apiPayments     = TrueAccordClient.retrievePayments();

    public static void main(String[] args) {
        final List<Debt> debts = Debt.of(apiDebts, apiPaymentPlans, apiPayments);
        debts.forEach(System.out::println);
    }
}
