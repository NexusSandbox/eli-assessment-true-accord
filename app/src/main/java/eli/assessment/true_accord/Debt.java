package eli.assessment.true_accord;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eli.assessment.true_accord.api.APIDebt;
import eli.assessment.true_accord.api.APIPayment;
import eli.assessment.true_accord.api.APIPaymentPlan;

import java.util.List;

public class Debt {
    public APIDebt          debt;
    @JsonProperty("is_in_payment_plan")
    public boolean          isInPaymentPlan;
    @JsonProperty("remaining_amount")
    public float            remainingAmount;
    @JsonProperty("next_payment_due_date")
    public String           nextPaymentDueDate;
    @JsonIgnore
    public APIPaymentPlan   paymentPlan;
    @JsonIgnore
    public List<APIPayment> payments;

    public Debt(APIDebt debt, APIPaymentPlan paymentPlan, List<APIPayment> payments) {

    }
}
