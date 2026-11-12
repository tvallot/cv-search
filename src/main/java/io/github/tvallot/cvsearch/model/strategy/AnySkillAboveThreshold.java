package io.github.tvallot.cvsearch.model.strategy;

import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;

/**
 * Selects applicants whose level is at least the threshold on one of the searched skills.
 */
public class AnySkillAboveThreshold extends ThresholdStrategy {

    /**
     * @param threshold minimum level required on at least one skill.
     */
    public AnySkillAboveThreshold(final int threshold) {
        super(threshold);
    }

    @Override
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        return applicant.skillsToEvaluate(skills).stream()
                .anyMatch(skill -> applicant.getSkill(skill) >= getThreshold());
    }
}
