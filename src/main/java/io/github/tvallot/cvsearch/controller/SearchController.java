package io.github.tvallot.cvsearch.controller;

import java.util.List;

import io.github.tvallot.cvsearch.model.SearchResult;
import io.github.tvallot.cvsearch.model.strategy.StrategyOption;

/**
 * What the views need from the controller.
 */
public interface SearchController {

    /** Returns the strategies the user can choose from. */
    List<StrategyOption> getStrategies();

    /**
     * Runs a search.
     *
     * @param option the selection strategy
     * @param skills the searched skills; when empty, all skills of each applicant are used
     * @return the selected applicants, best score first
     */
    List<SearchResult> search(StrategyOption option, List<String> skills);
}
