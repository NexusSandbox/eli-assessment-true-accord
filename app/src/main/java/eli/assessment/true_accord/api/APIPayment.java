package eli.assessment.true_accord.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class APIPayment {
    @JsonProperty("payment_plan_id")
    public int    paymentPlanId;
    public float  amount;
    public String date;

    public APIPayment() {
        // For JSON Initialization
    }

    public APIPayment(int paymentPlanId, float amount, String date) {
        this.paymentPlanId = paymentPlanId;
        this.amount        = amount;
        this.date          = date;
    }
}
