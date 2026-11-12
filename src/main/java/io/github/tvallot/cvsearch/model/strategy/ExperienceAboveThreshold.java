package io.github.tvallot.cvsearch.model.strategy;

import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;

/**
 * Selects applicants with at least a given number of years of experience on every
 * searched skill.
 */
public class ExperienceAboveThreshold extends ThresholdStrategy {

    /**
     * @param years minimum number of years of experience on each skill.
     */
    public ExperienceAboveThreshold(final int years) {
        super(years);
    }

    @Override
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        return applicant.skillsToEvaluate(skills).stream()
                .allMatch(skill -> applicant.yearsOfExperience(skill) >= getThreshold());
    }
}
