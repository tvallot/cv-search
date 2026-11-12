package io.github.tvallot.cvsearch.controller;

import java.util.List;

/**
 * Interface for ApplicantController to apply strategies.
 */
public interface ApplicantControllerInterface {
    /**
     * Apply the selected strategy to filter applicants.
     * @param strategyIndex The index of the selected strategy.
     * @param skills The list of skills to consider.
     * @return List of formatted results to display.
     */
    List<String> applyStrategy(int strategyIndex, List<String> skills);
}
