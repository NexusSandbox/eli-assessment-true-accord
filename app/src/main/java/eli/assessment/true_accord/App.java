package eli.assessment.true_accord;

import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;
import eli.assessment.true_accord.client.TrueAccordClient;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    final static List<APIDebt>        apiDebts        = TrueAccordClient.retrieveDebts();
    final static List<APIPaymentPlan> apiPaymentPlans = TrueAccordClient.retrievePaymentPlans();
    final static List<APIPayment>     apiPayments     = TrueAccordClient.retrievePayments();

    public static List<String> generateDebtsJson(List<Debt> debts) {
        return debts.stream().map(Debt::toString).collect(Collectors.toUnmodifiableList());
    }

    public static void main(String[] args) {
        final List<Debt> debts = Debt.of(apiDebts, apiPaymentPlans, apiPayments);
        generateDebtsJson(debts).forEach(System.out::println);
    }
}
