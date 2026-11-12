package io.github.tvallot.cvsearch.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Applicant, i.e. person having a name, a list of (skill, score) pairs, and a list of experiences.
 */
public class Applicant {
    private Map<String, Integer> skills = new HashMap<>();
    private List<Experience> experiences = new ArrayList<>();
    private String name;
    
    /**
     * Get the score for a given skill.
     */
    public int getSkill(final String skillName) {
        return skills.getOrDefault(skillName, 0);
    }

    /**
     * Get all skills of the applicant.
     */
    public Map<String, Integer> getSkills() {
        return skills;
    }

    /**
     * Assign score {@param value} to skill {@param skillName} for the current applicant.
     */
    public void setSkill(final String skillName, final int value) {
        skills.put(skillName, value);
    }

    /**
     * Get the name of the applicant.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the applicant.
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Add an experience to the applicant.
     * @param experience The experience to add.
     */
    public void addExperience(final Experience experience) {
        this.experiences.add(experience);
    }

    /**
     * Get the list of experiences of the applicant.
     */
    public List<Experience> getExperiences() {
        return experiences;
    }
}
