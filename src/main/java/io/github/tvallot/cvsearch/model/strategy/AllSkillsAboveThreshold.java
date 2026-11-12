package io.github.tvallot.cvsearch.model.strategy;

import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;

/**
 * Selects applicants whose level is at least the threshold on every searched skill.
 */
public class AllSkillsAboveThreshold extends ThresholdStrategy {

    /**
     * @param threshold minimum level required on each skill.
     */
    public AllSkillsAboveThreshold(final int threshold) {
        super(threshold);
    }

    @Override
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        return applicant.skillsToEvaluate(skills).stream()
                .allMatch(skill -> applicant.getSkill(skill) >= getThreshold());
    }
}
