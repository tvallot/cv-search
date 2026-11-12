package fr.univ_lyon1.info.m1.cv_search.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy that selects applicants whose the average skill level is above a certain threshold.
 */
public class AvgAboveTreshold extends AbstractTresholdStrategy {
    /**
     * Constructor setting the threshold value.
     * @param treshold Minimum average skill level required for selection.
     */
    public AvgAboveTreshold(final int treshold) {
        super(treshold);
    }

    /**
     * Determine if an applicant is selected based on the strategy.
     * @param applicant The applicant to evaluate.
     * @param skills List of skills to check.
     * @return true if the applicant meets the criteria, false otherwise.
     */
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        List<String> skillList = skills;
        if (skillList == null || skillList.isEmpty()) {
            skillList = new ArrayList<>(applicant.getSkills().keySet());
        }

        double sum = 0;
        int count = 0;
        for (String skill : skillList) {
            sum += applicant.getSkill(skill);
            count++;
        }
        double average = count == 0 ? 0 : sum / count;
        return average > getTreshold();
    }
}
