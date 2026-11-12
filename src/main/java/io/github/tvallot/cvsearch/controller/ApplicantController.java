package io.github.tvallot.cvsearch.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.tvallot.cvsearch.model.Applicant;
import io.github.tvallot.cvsearch.model.ApplicantList;
import io.github.tvallot.cvsearch.model.ApplicantListBuilder;
import io.github.tvallot.cvsearch.model.AvgAboveTreshold;
import io.github.tvallot.cvsearch.model.CompanyFactors;
import io.github.tvallot.cvsearch.model.Experience;
import io.github.tvallot.cvsearch.model.StrategyDescriptor;
import io.github.tvallot.cvsearch.model.AllAboveTreshold;
import io.github.tvallot.cvsearch.model.AnyAboveTreshold;
import io.github.tvallot.cvsearch.model.StrategyInterface;
import javafx.util.Pair;
import io.github.tvallot.cvsearch.model.ExperienceAboveTreshold;
import io.github.tvallot.cvsearch.utils.LanguageManager;


/**
 * Controller managing applicant selection based on strategies.
 */
public class ApplicantController implements ApplicantControllerInterface {
    private ApplicantList applicantList;
    private final List<StrategyDescriptor> strategies;

    /**
     * Constructor initializing the applicant list.
     */
    public ApplicantController() {
        this.applicantList = new ApplicantListBuilder(new File("data/applicants/")).build();
        this.strategies = Arrays.asList(
                new StrategyDescriptor("strategy_all", new AllAboveTreshold(60)),
                new StrategyDescriptor("strategy_avg", new AvgAboveTreshold(50)),
                new StrategyDescriptor("strategy_any", new AnyAboveTreshold(90)),
                new StrategyDescriptor("strategy_exp", new ExperienceAboveTreshold(5))
        );
    }

    /**
     * Get the list of available strategies.
     * @return List of strategy descriptors.
     */
    public List<StrategyDescriptor> getStrategies() {
        return strategies;
    }

    /**
     * Apply the selected strategy to filter applicants.
     * @param strategyIndex The index of the selected strategy.
     * @param skills The list of skills to search for.
     * @return List of formatted results to display.
     */
    @Override
    public List<String> applyStrategy(final int strategyIndex, final List<String> skills) {
        StrategyInterface strategy = strategies.get(strategyIndex).strategy();

        List<Pair<Applicant, Double>> scoredApplicants = new ArrayList<>();
        for (Applicant a : applicantList) {
            if (strategy.isSelected(a, skills)) {
                double mean = calculateMean(a, skills);
                scoredApplicants.add(new Pair<>(a, mean));
            }
        }

        // Tri décroissant par moyenne
        scoredApplicants.sort((p1, p2) -> Double.compare(p2.getValue(), p1.getValue()));

        List<String> results = new ArrayList<>();
        for (Pair<Applicant, Double> pair : scoredApplicants) {
            Applicant a = pair.getKey();

            double mean = pair.getValue();
            String smean = String.format("%.2f", mean);

            double score = getMeanSkillScore(a, skills);
            String sscore = String.format("%.2f", score);

            String text = a.getName() + " - "
                    + LanguageManager.getInstance().get("average") + ": "
                    + smean + " - Score: " + sscore;

            results.add(text);
        }
        return results;
    }

    /**
     * Get the skill score of an applicant, considering experience bonuses.
     * @param applicant The applicant whose skill score is calculated.
     * @param skills The skill to evaluate.
     * @return The total skill score including experience bonuses.
     */
    public double getSkillScore(final Applicant applicant, final List<String> skills) {
        List<String> skillList = skills;
        if (skillList == null || skillList.isEmpty()) {
            skillList = new ArrayList<>(applicant.getSkills().keySet());
        }
        double totalScore = 0;
        for (String skill : skillList) {
            int baseScore = applicant.getSkill(skill);
            double experienceBonus = 0.0;
            for (Experience exp : applicant.getExperiences()) {
                if (exp.getKeywords().contains(skill)) {
                    double factor = CompanyFactors.getFactor(exp.getCompany());
                    experienceBonus += exp.getDuration() * factor;
                }
            }
            totalScore += baseScore + experienceBonus;
        }
        return totalScore;
    }

    /**
     * Get the mean skill score of an applicant for a list of skills.
     * @param applicant The applicant whose skill score is calculated.
     * @param skills The list of skills to consider.
     * @return The mean skill score.
     */
    public double getMeanSkillScore(final Applicant applicant, final List<String> skills) {
        List<String> skillList = skills;
        if (skillList == null || skillList.isEmpty()) {
            skillList = new ArrayList<>(applicant.getSkills().keySet());
        }
        double sum = getSkillScore(applicant, skillList);
        return sum / skillList.size();
    }

    /**
     * Calculate the mean skill level of an applicant.
     * @param applicant The applicant whose skills are evaluated.
     * @param skills The list of skills to consider.
     * @return The mean skill level.
     */
    private double calculateMean(final Applicant applicant, final List<String> skills) {
        double sum = 0;
        int count = 0;
        if (skills.isEmpty()) {
            for (int score : applicant.getSkills().values()) {
                sum += score;
                count++;
            }
        } else {
            for (String skill : skills) {
                sum += applicant.getSkill(skill);
                count++;
            }
        }
        return count > 0 ? sum / count : 0;
    }
}
