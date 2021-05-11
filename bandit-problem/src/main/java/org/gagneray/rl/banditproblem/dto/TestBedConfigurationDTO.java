package org.gagneray.rl.banditproblem.dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.banditproblem.factories.ActionSelectionFactory;
import org.gagneray.rl.common.policies.ActionSectionPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestBedConfigurationDTO {

    public static final String ACTION_POLICIES_KEY = "policies";
    public static final String BANDIT_PROBLEM_COUNT_KEY = "banditProblemCount";
    public static final String BANDIT_PER_PROBLEM_KEY = "k";
    public static final String TOTAL_STEPS_KEY = "totalSteps";

    private List<ActionSectionPolicy<ArmedBandit>> policies = new ArrayList<>();
    private final int banditProblemCount;
    private final int k;
    private final int totalSteps;

    public TestBedConfigurationDTO(JsonNode json) {

        getPoliciesFromJson(json);
        this.banditProblemCount = Objects.requireNonNull(json).get(BANDIT_PROBLEM_COUNT_KEY).asInt();
        this.k = Objects.requireNonNull(json).get(BANDIT_PER_PROBLEM_KEY).asInt();
        this.totalSteps = Objects.requireNonNull(json).get(TOTAL_STEPS_KEY).asInt();

    }

    private void getPoliciesFromJson(JsonNode json) {

        if (!Objects.requireNonNull(json).get(ACTION_POLICIES_KEY).isArray()) {
            throw new IllegalArgumentException("Couldn't read policies from json array");
        }

        ActionSelectionFactory actionSelectionFactory = new ActionSelectionFactory();

        Objects.requireNonNull(json).get(ACTION_POLICIES_KEY)
                .forEach(jsonNode -> this.policies.add(actionSelectionFactory.getPolicy(jsonNode)));
    }

    public List<ActionSectionPolicy<ArmedBandit>> getPolicies() {
        return policies;
    }

    public int getBanditProblemCount() {
        return banditProblemCount;
    }

    public int getK() {
        return k;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBedConfigurationDTO that = (TestBedConfigurationDTO) o;
        return banditProblemCount == that.banditProblemCount && k == that.k && totalSteps == that.totalSteps && Objects.equals(policies, that.policies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policies, banditProblemCount, k, totalSteps);
    }

    @Override
    public String toString() {
        return "TestBedConfigurationDTO{" +
                "policies=" + policies +
                ", banditProblemCount=" + banditProblemCount +
                ", k=" + k +
                ", totalSteps=" + totalSteps +
                '}';
    }
}
