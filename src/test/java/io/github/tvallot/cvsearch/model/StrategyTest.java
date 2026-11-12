package io.github.tvallot.cvsearch.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.tvallot.cvsearch.model.strategy.AllSkillsAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.AnySkillAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.AverageAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.ExperienceAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.SelectionStrategy;

/**
 * Tests for the selection strategies.
 *
 * <p>John Smith (applicant1.yaml) has c: 90, c++: 70, java: 50, and five years at
 * Google using c, c++ and java.
 */
class StrategyTest {
    private Applicant john;

    @BeforeEach
    void setUp() {
        john = new ApplicantBuilder("data/applicants/applicant1.yaml").build();
    }

    @Test
    void thresholdMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> new AllSkillsAboveThreshold(-5));
        assertThrows(IllegalArgumentException.class, () -> new AnySkillAboveThreshold(-5));
        assertThrows(IllegalArgumentException.class, () -> new AverageAboveThreshold(-5));
        assertThrows(IllegalArgumentException.class, () -> new ExperienceAboveThreshold(-5));
    }

    @Test
    void emptySearchUsesAllSkills() {
        List<String> noSkills = List.of();

        assertSelected(noSkills,
                new AllSkillsAboveThreshold(40),
                new AnySkillAboveThreshold(80),
                new AverageAboveThreshold(50),
                new ExperienceAboveThreshold(5));
        assertRejected(noSkills,
                new AllSkillsAboveThreshold(90),
                new AnySkillAboveThreshold(100),
                new AverageAboveThreshold(80),
                new ExperienceAboveThreshold(10));
    }

    @Test
    void searchOnGivenSkills() {
        List<String> skills = List.of("java", "c++");

        assertSelected(skills,
                new AllSkillsAboveThreshold(40),
                new AnySkillAboveThreshold(50),
                new AverageAboveThreshold(50),
                new ExperienceAboveThreshold(3));
        assertRejected(skills,
                new AllSkillsAboveThreshold(60),
                new AnySkillAboveThreshold(80),
                new AverageAboveThreshold(70),
                new ExperienceAboveThreshold(6));
    }

    @Test
    void thresholdIsInclusive() {
        // java + c++ average is exactly 60, java is exactly 50.
        List<String> skills = List.of("java", "c++");

        assertSelected(skills,
                new AverageAboveThreshold(60),
                new AllSkillsAboveThreshold(50),
                new AnySkillAboveThreshold(70),
                new ExperienceAboveThreshold(5));
    }

    @Test
    void unknownSkillCountsAsZero() {
        List<String> skills = List.of("c", "rust");

        assertSelected(skills, new AnySkillAboveThreshold(90));
        assertRejected(skills, new AllSkillsAboveThreshold(10), new ExperienceAboveThreshold(1));
    }

    private void assertSelected(final List<String> skills, final SelectionStrategy... strategies) {
        for (SelectionStrategy strategy : strategies) {
            assertThat(strategy.getClass().getSimpleName(),
                    strategy.isSelected(john, skills), is(true));
        }
    }

    private void assertRejected(final List<String> skills, final SelectionStrategy... strategies) {
        for (SelectionStrategy strategy : strategies) {
            assertThat(strategy.getClass().getSimpleName(),
                    strategy.isSelected(john, skills), is(false));
        }
    }
}
