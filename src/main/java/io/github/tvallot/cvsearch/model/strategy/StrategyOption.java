package io.github.tvallot.cvsearch.model.strategy;

/**
 * A strategy offered to the user, with the key of its label in the translation files.
 *
 * @param labelKey key of the label in {@code labels_*.properties}
 * @param strategy the selection rule
 */
public record StrategyOption(String labelKey, SelectionStrategy strategy) {
}
