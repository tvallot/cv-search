package io.github.tvallot.cvsearch.model;

import java.util.List;

/**
 * Computes the figures displayed for each applicant in the search results.
 */
public final class ScoreCalculator {

    private ScoreCalculator() { }

    /**
     * Returns the average skill level of the applicant over the given skills
     * (all of their skills if the list is empty).
     */
    public static double averageLevel(final Applicant applicant, final List<String> skills) {
        return applicant.skillsToEvaluate(skills).stream()
                .mapToInt(applicant::getSkill)
                .average()
                .orElse(0);
    }

    /**
     * Returns the score of the applicant: for each skill, its level plus an experience
     * bonus, averaged over the given skills (all of their skills if the list is empty).
     */
    public static double score(final Applicant applicant, final List<String> skills) {
        return applicant.skillsToEvaluate(skills).stream()
                .mapToDouble(skill -> applicant.getSkill(skill) + experienceBonus(applicant, skill))
                .average()
                .orElse(0);
    }

    /**
     * Returns the experience bonus for a skill: years spent using it, weighted by company.
     */
    public static double experienceBonus(final Applicant applicant, final String skill) {
        return applicant.getExperiences().stream()
                .filter(exp -> exp.keywords().contains(skill))
                .mapToDouble(exp -> exp.duration() * CompanyFactors.getFactor(exp.company()))
                .sum();
    }
}
