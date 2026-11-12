package io.github.tvallot.cvsearch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A job applicant: a name, a level (0-100) for each skill, and past work experiences.
 */
public class Applicant {
    private final Map<String, Integer> skills = new HashMap<>();
    private final List<Experience> experiences = new ArrayList<>();
    private String name;

    /**
     * Returns the level of the applicant for a skill, or 0 if the skill is unknown.
     */
    public int getSkill(final String skillName) {
        return skills.getOrDefault(skillName, 0);
    }

    /** Returns all skills of the applicant with their level (read-only view). */
    public Map<String, Integer> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    /** Sets the level of the applicant for a skill. */
    public void setSkill(final String skillName, final int level) {
        skills.put(skillName, level);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /** Adds a work experience to the applicant. */
    public void addExperience(final Experience experience) {
        experiences.add(experience);
    }

    /** Returns the work experiences of the applicant (read-only view). */
    public List<Experience> getExperiences() {
        return Collections.unmodifiableList(experiences);
    }

    /**
     * Returns the skills to evaluate for a search: the requested ones, or every skill
     * of the applicant when nothing was requested.
     */
    public List<String> skillsToEvaluate(final List<String> requested) {
        if (requested == null || requested.isEmpty()) {
            return List.copyOf(skills.keySet());
        }
        return requested;
    }

    /** Returns the total number of years during which the applicant used a skill at work. */
    public int yearsOfExperience(final String skill) {
        return experiences.stream()
                .filter(exp -> exp.keywords().contains(skill))
                .mapToInt(Experience::duration)
                .sum();
    }
}
