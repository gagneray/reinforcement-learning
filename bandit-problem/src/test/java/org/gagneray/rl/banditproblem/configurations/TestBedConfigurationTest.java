package org.gagneray.rl.banditproblem.configurations;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.EpsilonGreedy;
import org.gagneray.rl.banditproblem.actionSelectionPolicies.UpperConfidenceBound;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.policies.ActionSectionPolicy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBedConfigurationTest {

    @Test
    public void createTestBedConfiguration() {

        ActionSectionPolicy<ArmedBandit> policy1 = new EpsilonGreedy(0.1);
        ActionSectionPolicy<ArmedBandit> policy2 = new UpperConfidenceBound(2);
        List<ActionSectionPolicy<ArmedBandit>> policies = new ArrayList<>();
        policies.add(policy1);
        policies.add(policy2);

        TestBedConfiguration testBedConfiguration = new TestBedConfiguration(policies, 2000, 10, 1000);
        assertEquals(1000, testBedConfiguration.getTotalSteps());
        assertEquals(2000, testBedConfiguration.getBanditProblemCount());
        assertEquals(2, testBedConfiguration.getBanditProblemConfigurations().size());
        assertEquals(10, testBedConfiguration.getBanditProblemConfigurations().get(0).getGaussianDistributions().size());
        assertEquals(10, testBedConfiguration.getBanditProblemConfigurations().get(1).getGaussianDistributions().size());
    }

}