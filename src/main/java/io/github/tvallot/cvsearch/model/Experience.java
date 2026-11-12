package io.github.tvallot.cvsearch.model;

import java.util.List;
/**
 * Experience held by an applicant.
 */
public class Experience {
    private String company;
    private int start;
    private int end;
    private List<String> keywords;

    /**
     * Constructor.
     * @param company The company where the experience was gained.
     * @param start The start year of the experience.
     * @param end The end year of the experience.
     * @param keywords The list of keywords associated with the experience.
     */
    public Experience(final String company, 
        final int start, final int end, final List<String> keywords) {
        this.company = company;
        this.start = start;
        this.end = end;
        this.keywords = keywords;
    }

    /**
     * Get the company name.
     * @return The company name.
     */
    public String getCompany() {
        return company;
    }
    
    /**
     * Get the start year, end year and keywords.
     * @return The start year, end year and keywords.
     */
    public int getStart() {
        return start;
    }

    /**
     * Get the end year.
     * @return The end year.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Get the keywords.
     * @return The keywords.
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Get the duration of the experience in years.
     * @return The duration in years.
     */
    public int getDuration() {
        return end - start;
    }
}
