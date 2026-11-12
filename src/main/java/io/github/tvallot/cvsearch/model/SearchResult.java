package io.github.tvallot.cvsearch.model;

/**
 * One line of the search results.
 *
 * @param applicantName name of the selected applicant
 * @param average       average level over the searched skills
 * @param score         level plus experience bonus, averaged over the searched skills
 */
public record SearchResult(String applicantName, double average, double score) {
}
