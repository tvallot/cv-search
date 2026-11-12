package fr.univ_lyon1.info.m1.cv_search;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import fr.univ_lyon1.info.m1.cv_search.model.AllAboveTreshold;
import fr.univ_lyon1.info.m1.cv_search.model.AnyAboveTreshold;
import fr.univ_lyon1.info.m1.cv_search.model.StrategyInterface;
import fr.univ_lyon1.info.m1.cv_search.model.Applicant;
import fr.univ_lyon1.info.m1.cv_search.model.ApplicantBuilder;
import fr.univ_lyon1.info.m1.cv_search.model.AvgAboveTreshold;
import fr.univ_lyon1.info.m1.cv_search.model.ExperienceAboveTreshold;

/**
 * Tests for the Applicant class.
 */
class StrategyTest {
    private Applicant applicant;

    @BeforeEach
    void setUp() {
        applicant = new ApplicantBuilder("data/applicants/applicant1.yaml").build();
    }

    /** Test that tresholds must be positive. */
    @Test
    void tresholdNegativeTest() {
        Class<IllegalArgumentException> expected = 
            IllegalArgumentException.class;

        assertThrows(expected, () -> new AllAboveTreshold(-5));
        assertThrows(expected, () -> new AnyAboveTreshold(-5));
        assertThrows(expected, () -> new AvgAboveTreshold(-5));
        assertThrows(expected, () -> new ExperienceAboveTreshold(-5));
    }

    @Test
    void emptyListStrategy() {
        List<StrategyInterface> trueStrategies = List.of(
            new AllAboveTreshold(40),
            new AnyAboveTreshold(80),
            new AvgAboveTreshold(50),
            new ExperienceAboveTreshold(5)
        );

        List<StrategyInterface> falseStrategies = List.of(
            new AllAboveTreshold(90),
            new AnyAboveTreshold(100),
            new AvgAboveTreshold(80),
            new ExperienceAboveTreshold(10)
        );
        List<String> skills = null;
        for (StrategyInterface strategy : trueStrategies) {
            assertThat(strategy.isSelected(applicant, skills), is(true));
        }

        for (StrategyInterface strategy : falseStrategies) {
            assertThat(strategy.isSelected(applicant, skills), is(false));
        }
    }

    @Test
    void elementsListStrategy() {
        List<StrategyInterface> trueStrategies = List.of(
            new AllAboveTreshold(40),
            new AnyAboveTreshold(50),
            new AvgAboveTreshold(50),
            new ExperienceAboveTreshold(3)
        );

        List<StrategyInterface> falseStrategies = List.of(
            new AllAboveTreshold(60),
            new AnyAboveTreshold(80),
            new AvgAboveTreshold(70),
            new ExperienceAboveTreshold(6)
        );
    
        List<String> skills = List.of("java", "c++");

        for (StrategyInterface strategy : trueStrategies) {
            assertThat(strategy.isSelected(applicant, skills), is(true));
        }

        for (StrategyInterface strategy : falseStrategies) {
            assertThat(strategy.isSelected(applicant, skills), is(false));
        }
    }
}
