package io.github.tvallot.cvsearch.model.strategy;

/**
 * Base class for strategies comparing a value to a fixed threshold.
 */
public abstract class ThresholdStrategy implements SelectionStrategy {
    private final int threshold;

    /**
     * @param threshold the threshold, must be positive or zero.
     */
    protected ThresholdStrategy(final int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must be positive, got " + threshold);
        }
        this.threshold = threshold;
    }

    protected int getThreshold() {
        return threshold;
    }
}
