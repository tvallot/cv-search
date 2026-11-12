package io.github.tvallot.cvsearch.model;

import java.util.Map;

/**
 * Weight given to a year of experience depending on the company.
 *
 * <p>This rule is deliberately simple and arbitrary. It is the kind of rule that makes
 * automated CV screening biased, so it is kept as a small table that is easy to see
 * and change.
 */
public final class CompanyFactors {
    private static final double DEFAULT_FACTOR = 1.0;

    private static final Map<String, Double> FACTORS = Map.of(
            "Google", 2.0,
            "Facebook", 2.0,
            "Apple", 2.0,
            "Amazon", 2.0,
            "Microsoft", 2.0,
            "Capgemini", 1.2,
            "Sopra Steria", 1.2,
            "Atos", 1.2);

    private CompanyFactors() { }

    /** Returns the weight of a company, 1.0 for companies that are not listed. */
    public static double getFactor(final String company) {
        return FACTORS.getOrDefault(company, DEFAULT_FACTOR);
    }
}
