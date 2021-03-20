package eli.assessment.true_accord.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class APIPaymentPlan {
    public int                  id;
    @JsonProperty("debt_id")
    public int                  debtId;
    @JsonProperty("amount_to_pay")
    public float                amountToPay;
    @JsonProperty("installment_frequency")
    public InstallmentFrequency installmentFrequency;
    @JsonProperty("installment_amount")
    public float                installmentAmount;
    @JsonProperty("start_date")
    public String               startDate;

    public APIPaymentPlan() {
        // For JSON Initialization
    }

    public APIPaymentPlan(int id, int debtId, float amountToPay, InstallmentFrequency installmentFrequency,
                          float installmentAmount, String startDate) {
        this.id                   = id;
        this.debtId               = debtId;
        this.amountToPay          = amountToPay;
        this.installmentFrequency = installmentFrequency;
        this.installmentAmount    = installmentAmount;
        this.startDate            = startDate;
    }
}
