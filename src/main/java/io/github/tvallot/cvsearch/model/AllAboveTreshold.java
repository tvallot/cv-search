package io.github.tvallot.cvsearch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy that selects applicants whose skills are all above a certain threshold.
 */
public class AllAboveTreshold extends AbstractTresholdStrategy {
    /**
     * Constructor setting the threshold value.
     * @param treshold Minimum skill level required for selection.
     */
    public AllAboveTreshold(final int treshold) {
        super(treshold);
    }

    /**
     * Determine if an applicant is selected based on the strategy.
     * @param applicant The applicant to evaluate.
     * @param skills List of skills to check.
     * @return true if the applicant meets the criteria, false otherwise.
     */
    @Override
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        List<String> skillList = skills;
        if (skillList == null || skillList.isEmpty()) {
            skillList = new ArrayList<>(applicant.getSkills().keySet());
        }
        for (String skill : skillList) {
            if (applicant.getSkill(skill) < getTreshold()) {
                return false;
            }
        }
        return true;
    }
}
