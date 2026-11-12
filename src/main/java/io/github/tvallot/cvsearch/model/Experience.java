package io.github.tvallot.cvsearch.model;

import java.util.List;

/**
 * A work experience: a company, a period (in years) and the skills used there.
 *
 * @param company  name of the company
 * @param start    first year
 * @param end      last year
 * @param keywords skills used during this experience
 */
public record Experience(String company, int start, int end, List<String> keywords) {

    /**
     * Validates the period and makes the keyword list immutable.
     */
    public Experience {
        if (end < start) {
            throw new IllegalArgumentException(
                    "Experience at " + company + " ends before it starts");
        }
        keywords = keywords == null ? List.of() : List.copyOf(keywords);
    }

    /** Returns the duration of the experience, in years. */
    public int duration() {
        return end - start;
    }
}
