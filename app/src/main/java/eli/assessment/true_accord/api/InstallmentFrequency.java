package eli.assessment.true_accord.api;

public enum InstallmentFrequency {
    WEEKLY(7),
    BI_WEEKLY(14);

    final public int interval;

    InstallmentFrequency(final int interval) {
        this.interval = interval;
    }
}
