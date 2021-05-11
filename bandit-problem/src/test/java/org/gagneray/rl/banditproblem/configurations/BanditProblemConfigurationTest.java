package org.gagneray.rl.banditproblem.configurations;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.EpsilonGreedy;
import org.gagneray.rl.banditproblem.actionSelectionPolicies.UpperConfidenceBound;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.distributions.GaussianDistribution;
import org.gagneray.rl.common.policies.ActionSectionPolicy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BanditProblemConfigurationTest {


    @Test
    public void createConfigWithDefaultDistributions() {
        ActionSectionPolicy<ArmedBandit> policy = new EpsilonGreedy(0.1);
        BanditProblemConfiguration conf = new BanditProblemConfiguration(policy, 10);
    }

    @Test
    public void createConfigProvidingDistributions() {
        ActionSectionPolicy<ArmedBandit> policy = new UpperConfidenceBound(2);
        List<GaussianDistribution> distributions = new ArrayList<>();
        distributions.add(new GaussianDistribution(0, 1));
        distributions.add(new GaussianDistribution(1, 1));
        distributions.add(new GaussianDistribution(2, 1));
        distributions.add(new GaussianDistribution(3, 1));
        BanditProblemConfiguration conf = new BanditProblemConfiguration(policy, distributions);
    }

}