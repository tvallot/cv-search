package io.github.tvallot.cvsearch.model;

/**
 * Abstract strategy class that uses a threshold value.
 */
public abstract class AbstractTresholdStrategy implements StrategyInterface {
    private int treshold;

    /**
     * Constructor setting the threshold value.
     * @param treshold The threshold value for the strategy.
     */
    protected AbstractTresholdStrategy(final int treshold) {
        if (treshold < 0) {
            throw new IllegalArgumentException("Threshold must be positive.");
        }
        this.treshold = treshold;
    }

    /**
     * Get the threshold value.
     */
    protected int getTreshold() {
        return treshold;
    }
}
