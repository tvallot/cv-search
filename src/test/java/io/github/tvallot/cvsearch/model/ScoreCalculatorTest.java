package io.github.tvallot.cvsearch.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ScoreCalculator}.
 */
class ScoreCalculatorTest {
    private static final double EPSILON = 1e-9;

    private Applicant applicant;

    @BeforeEach
    void setUp() {
        applicant = new Applicant();
        applicant.setSkill("java", 80);
        applicant.setSkill("go", 40);
        // Google has a factor of 2.0, unknown companies 1.0.
        applicant.addExperience(new Experience("Google", 2010, 2014, List.of("java")));
        applicant.addExperience(new Experience("Startup", 2014, 2017, List.of("java", "go")));
    }

    @Test
    void averageLevelOfSearchedSkills() {
        assertThat(ScoreCalculator.averageLevel(applicant, List.of("java", "go")),
                closeTo(60, EPSILON));
        assertThat(ScoreCalculator.averageLevel(applicant, List.of("java", "rust")),
                closeTo(40, EPSILON));
    }

    @Test
    void experienceBonusIsWeightedByCompany() {
        // 4 years at Google x 2.0 + 3 years elsewhere x 1.0
        assertThat(ScoreCalculator.experienceBonus(applicant, "java"), closeTo(11, EPSILON));
        assertThat(ScoreCalculator.experienceBonus(applicant, "go"), closeTo(3, EPSILON));
    }

    @Test
    void scoreAddsExperienceToLevel() {
        // java: 80 + 11, go: 40 + 3
        assertThat(ScoreCalculator.score(applicant, List.of("java", "go")),
                closeTo(67, EPSILON));
    }

    @Test
    void applicantWithoutSkillsScoresZero() {
        assertThat(ScoreCalculator.score(new Applicant(), List.of()), is(0.0));
        assertThat(ScoreCalculator.averageLevel(new Applicant(), List.of()), is(0.0));
    }
}
