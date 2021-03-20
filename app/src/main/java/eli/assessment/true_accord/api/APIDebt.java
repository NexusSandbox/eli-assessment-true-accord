package eli.assessment.true_accord.api;

public class APIDebt {
    public int   id;
    public float amount;

    public APIDebt() {
        // For JSON Initialization
    }

    public APIDebt(int id, float amount) {
        this.id     = id;
        this.amount = amount;
    }
}
