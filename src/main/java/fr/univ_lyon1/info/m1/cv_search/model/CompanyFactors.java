package fr.univ_lyon1.info.m1.cv_search.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Company factors used to weight experience from different companies.
 */
public final class CompanyFactors {
    private CompanyFactors() { }

    public static final Map<String, Double> FACTORS = new HashMap<>();
    static {
        FACTORS.put("Google", 2.0);
        FACTORS.put("Facebook", 2.0);
        FACTORS.put("Apple", 2.0);
        FACTORS.put("Amazon", 2.0);
        FACTORS.put("Microsoft", 2.0);
        // Exemple ESN
        FACTORS.put("Capgemini", 1.2);
        FACTORS.put("Sopra Steria", 1.2);
        FACTORS.put("Atos", 1.2);
        // Par défaut, 1.0
    }

    /**
     * Get the factor for a given company.
     * @param company The company name.
     * @return The factor associated with the company.
     */
    public static double getFactor(final String company) {
        return FACTORS.getOrDefault(company, 1.0);
    }
}
