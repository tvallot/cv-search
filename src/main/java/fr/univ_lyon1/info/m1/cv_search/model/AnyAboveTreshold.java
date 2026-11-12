package fr.univ_lyon1.info.m1.cv_search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy that selects applicants whose skills have at least one above a certain threshold.
 */
public class AnyAboveTreshold extends AbstractTresholdStrategy {

    /**
     * Constructor setting the threshold value.
     * @param treshold Minimum skill level required for selection.
     */
    public AnyAboveTreshold(final int treshold) {
        super(treshold);
    }

    /**
     * Determine if an applicant is selected based on the strategy.
     * @param applicant The applicant to evaluate.
     * @param skills List of skills to check.
     * @return true if the applicant meets the criteria, false otherwise.
     */
    @Override
    public boolean isSelected(final Applicant applicant, final java.util.List<String> skills) {
        List<String> skillList = skills;
        if (skillList == null || skillList.isEmpty()) {
            skillList = new ArrayList<>(applicant.getSkills().keySet());
        }
        for (String skill : skillList) {
            if (applicant.getSkill(skill) >= getTreshold()) {
                return true;
            }
        }
        return false;
    }
}
