package io.github.tvallot.cvsearch.model.strategy;

import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;

/**
 * A rule deciding whether an applicant matches a search.
 */
public interface SelectionStrategy {

    /**
     * Tells whether the applicant is selected.
     *
     * @param applicant the applicant to evaluate
     * @param skills    the searched skills; when empty, all skills of the applicant are used
     */
    boolean isSelected(Applicant applicant, List<String> skills);
}
