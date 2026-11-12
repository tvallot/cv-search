package io.github.tvallot.cvsearch.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.tvallot.cvsearch.model.Applicant;
import io.github.tvallot.cvsearch.model.ApplicantList;
import io.github.tvallot.cvsearch.model.SearchResult;
import io.github.tvallot.cvsearch.model.strategy.StrategyOption;

/**
 * Tests for {@link ApplicantController}.
 */
class ApplicantControllerTest {
    private ApplicantController controller;

    @BeforeEach
    void setUp() {
        ApplicantList list = new ApplicantList();
        list.add(applicant("Bob", 70));
        list.add(applicant("Alice", 95));
        list.add(applicant("Carol", 30));
        list.add(applicant("Dan", 70));
        controller = new ApplicantController(list);
    }

    @Test
    void offersFourStrategies() {
        assertThat(controller.getStrategies().size(), is(4));
    }

    @Test
    void returnsSelectedApplicantsBestFirst() {
        List<SearchResult> results = controller.search(strategy("strategy_all"), List.of("java"));

        // Carol is below 60; Bob and Dan are tied and sorted by name.
        assertThat(results.stream().map(SearchResult::applicantName).toList(),
                contains("Alice", "Bob", "Dan"));
        assertThat(results.get(0).average(), is(95.0));
    }

    @Test
    void returnsNothingWhenNobodyMatches() {
        assertThat(controller.search(strategy("strategy_any"), List.of("rust")), is(empty()));
    }

    private StrategyOption strategy(final String key) {
        return controller.getStrategies().stream()
                .filter(option -> option.labelKey().equals(key))
                .findFirst()
                .orElseThrow();
    }

    private static Applicant applicant(final String name, final int javaLevel) {
        Applicant a = new Applicant();
        a.setName(name);
        a.setSkill("java", javaLevel);
        return a;
    }
}
