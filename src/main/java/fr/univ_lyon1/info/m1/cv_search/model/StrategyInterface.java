package fr.univ_lyon1.info.m1.cv_search.model;

import java.util.List;

/**
 * Interface for selection strategies based on applicant skills.
 */
public interface StrategyInterface {
    /**
     * Determine if an applicant is selected based on the strategy.
     * @param applicant The applicant to evaluate.
     * @param skills List of skills to check.
     * @return true if the applicant meets the criteria, false otherwise.
     */
    boolean isSelected(Applicant applicant, List<String> skills);
}
