package io.github.tvallot.cvsearch.model;
import io.github.tvallot.cvsearch.utils.LanguageManager;

/**
 * Descriptor for a strategy, containing its label and implementation.
 */
public class StrategyDescriptor {
    private final String label;
    private final StrategyInterface strategy;

    /**
     * Constructor for StrategyDescriptor.
     * @param label The label of the strategy.
     * @param strategy The strategy implementation.
     */
    public StrategyDescriptor(final String label, final StrategyInterface strategy) {
        this.label = label;
        this.strategy = strategy;
    }

    /**
     * Get the label of the strategy.
     * @return The strategy label.
     */
    public String label() {
        return label;
    }

    /**
     * Get the strategy implementation.
     * @return The strategy.
     */
    public StrategyInterface strategy() {
        return strategy;
    }

    /**
     * String representation of the strategy descriptor.
     * @return The strategy label.
     */
    @Override
    public String toString() {
        return LanguageManager.getInstance().get(label);
    }
}
