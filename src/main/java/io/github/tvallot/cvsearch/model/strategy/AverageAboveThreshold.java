package io.github.tvallot.cvsearch.model.strategy;

import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;
import io.github.tvallot.cvsearch.model.ScoreCalculator;

/**
 * Selects applicants whose average level over the searched skills is at least the threshold.
 */
public class AverageAboveThreshold extends ThresholdStrategy {

    /**
     * @param threshold minimum average level.
     */
    public AverageAboveThreshold(final int threshold) {
        super(threshold);
    }

    @Override
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        return ScoreCalculator.averageLevel(applicant, skills) >= getThreshold();
    }
}
