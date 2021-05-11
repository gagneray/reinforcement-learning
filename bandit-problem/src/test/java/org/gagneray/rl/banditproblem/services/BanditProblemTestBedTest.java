package org.gagneray.rl.banditproblem.services;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.EpsilonGreedy;
import org.gagneray.rl.banditproblem.actionSelectionPolicies.UpperConfidenceBound;
import org.gagneray.rl.banditproblem.configurations.BanditProblemConfiguration;
import org.gagneray.rl.banditproblem.configurations.TestBedConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class BanditProblemTestBedTest {

    @Test
    public void createTestBedAndProcess() {

        List<BanditProblemConfiguration> configurations = Arrays.asList(
                new BanditProblemConfiguration(new EpsilonGreedy(0.1), 10),
                new BanditProblemConfiguration(new UpperConfidenceBound(2), 10)
        );

        TestBedConfiguration testBedConfiguration = new TestBedConfiguration(configurations, 1000, 2000);
        BanditProblemTestBed testBed = new BanditProblemTestBed(testBedConfiguration);
        testBed.process();
    }
}