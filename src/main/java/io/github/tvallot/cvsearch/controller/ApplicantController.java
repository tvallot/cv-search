package io.github.tvallot.cvsearch.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;
import io.github.tvallot.cvsearch.model.ApplicantList;
import io.github.tvallot.cvsearch.model.ScoreCalculator;
import io.github.tvallot.cvsearch.model.SearchResult;
import io.github.tvallot.cvsearch.model.strategy.AllSkillsAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.AnySkillAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.AverageAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.ExperienceAboveThreshold;
import io.github.tvallot.cvsearch.model.strategy.StrategyOption;

/**
 * Runs searches on a list of applicants. A single instance is shared by all windows.
 */
public class ApplicantController implements SearchController {

    private static final List<StrategyOption> STRATEGIES = List.of(
            new StrategyOption("strategy_all", new AllSkillsAboveThreshold(60)),
            new StrategyOption("strategy_avg", new AverageAboveThreshold(50)),
            new StrategyOption("strategy_any", new AnySkillAboveThreshold(90)),
            new StrategyOption("strategy_exp", new ExperienceAboveThreshold(5)));

    private static final Comparator<SearchResult> BEST_FIRST =
            Comparator.comparingDouble(SearchResult::score).reversed()
                    .thenComparing(SearchResult::applicantName);

    private final ApplicantList applicants;

    /**
     * @param applicants the applicants to search in.
     */
    public ApplicantController(final ApplicantList applicants) {
        this.applicants = applicants;
    }

    @Override
    public List<StrategyOption> getStrategies() {
        return STRATEGIES;
    }

    @Override
    public List<SearchResult> search(final StrategyOption option, final List<String> skills) {
        List<SearchResult> results = new ArrayList<>();
        for (Applicant applicant : applicants) {
            if (option.strategy().isSelected(applicant, skills)) {
                results.add(new SearchResult(
                        applicant.getName(),
                        ScoreCalculator.averageLevel(applicant, skills),
                        ScoreCalculator.score(applicant, skills)));
            }
        }
        results.sort(BEST_FIRST);
        return results;
    }
}
