package org.gagneray.rl.banditproblem.configurations;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.UpperConfidenceBound;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.distributions.GaussianDistribution;
import org.gagneray.rl.common.policies.ActionSectionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class BanditProblemConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(BanditProblemConfiguration.class);

    public final ActionSectionPolicy<ArmedBandit> actionSectionPolicy;
    public final int k;
    public final List<GaussianDistribution> gaussianDistributions;

    public BanditProblemConfiguration(ActionSectionPolicy<ArmedBandit> actionSectionPolicy, List<GaussianDistribution> gaussianDistributions) {

        LOGGER.trace("Create BanditProblem Configuration from policy {} and distributions {}", actionSectionPolicy, gaussianDistributions);

        this.actionSectionPolicy = actionSectionPolicy;
        this.k = gaussianDistributions.size();
        this.gaussianDistributions = gaussianDistributions;
    }

    public BanditProblemConfiguration(ActionSectionPolicy<ArmedBandit> actionSectionPolicy, int k) {

        LOGGER.trace("Create BanditProblem Configuration from policy {} and k={}", actionSectionPolicy, k);

        this.actionSectionPolicy = actionSectionPolicy;
        this.k = k;
        this.gaussianDistributions = GaussianDistribution.generateDefaultDistributions(k);
    }

    public ActionSectionPolicy<ArmedBandit> getActionSectionPolicy() {
        return actionSectionPolicy;
    }

    public int getK() {
        return k;
    }

    public List<GaussianDistribution> getGaussianDistributions() {
        return gaussianDistributions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanditProblemConfiguration that = (BanditProblemConfiguration) o;
        return k == that.k && Objects.equals(actionSectionPolicy, that.actionSectionPolicy) && Objects.equals(gaussianDistributions, that.gaussianDistributions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionSectionPolicy, k, gaussianDistributions);
    }

    @Override
    public String toString() {
        return "BanditProblemConfiguration{" +
                "actionSectionPolicy=" + actionSectionPolicy +
                ", k=" + k +
                ", gaussianDistributions=" + gaussianDistributions +
                '}';
    }
}
