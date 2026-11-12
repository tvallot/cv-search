package fr.univ_lyon1.info.m1.cv_search.model;

import java.util.List;

/**
 * Strategy selecting applicants based on experience duration for given skills.
 */
public class ExperienceAboveTreshold extends AbstractTresholdStrategy {

    /**
     * Constructor setting the minimum years of experience required.
     * @param treshold The minimum years of experience threshold.
     */
    public ExperienceAboveTreshold(final int treshold) {
        super(treshold);
    }

    /**
     * Determine if an applicant meets the experience threshold for the specified skills.
     * @param applicant The applicant to evaluate.
     * @param skills The list of skills to consider.
     * @return True if the applicant meets the threshold for all specified skills, false otherwise.
     */
    @Override
    public boolean isSelected(final Applicant applicant, final List<String> skills) {
        List<String> skillList = skills;
        if (skillList == null || skillList.isEmpty()) {
            skillList = List.copyOf(applicant.getSkills().keySet());
        }
        for (String skill : skillList) {
            int totalYears = 0;
            for (Experience exp : applicant.getExperiences()) {
                if (exp.getKeywords().contains(skill)) {
                    totalYears += exp.getDuration();
                }
            }
            if (totalYears < getTreshold()) {
                return false;
            }
        }
        return true;
    }
}
