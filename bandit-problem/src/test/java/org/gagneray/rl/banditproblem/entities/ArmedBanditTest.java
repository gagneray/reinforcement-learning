package org.gagneray.rl.banditproblem.entities;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.EpsilonGreedy;
import org.gagneray.rl.common.distributions.GaussianDistribution;
import org.gagneray.rl.common.rewards.Reward;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ArmedBanditTest {

    @Test
    public void shouldHaveDefaultValuesWhenDefaultArmedBandit() {

        ArmedBandit armedBandit = new ArmedBandit();

        assertEquals(0, armedBandit.getRewardDistribution().getMean());
        assertEquals(1, armedBandit.getRewardDistribution().getVariance());
    }

    @Test
    public void shouldCustomizedValuesWhenProvidedRewardDistribution() {

        ArmedBandit armedBandit = new ArmedBandit(new GaussianDistribution(3, 4));

        assertEquals(3, armedBandit.getRewardDistribution().getMean());
        assertEquals(4, armedBandit.getRewardDistribution().getVariance());
        assertEquals(2, armedBandit.getRewardDistribution().getStandardDeviation());
    }

    /**
     * Test whether probability of empirical observed reward values follow a gaussian distribution : 68.27% of values should be away from one standard deviation
     * Note : could be statistically tested with a normality test such as qui² or Lilliefors tests
     */
    @Test
    public void RewardsShouldFollowGaussianWhenPerformActions() {

        ArmedBandit armedBandit = new ArmedBandit(new GaussianDistribution(0, 1));

        int N = 10000;
        int i = 0;
        int obsCenteredValues = 0;
        while (i < N) {
            Reward reward = armedBandit.performAction();

            if (Math.abs(reward.getValue()) < armedBandit.getRewardDistribution().getMean() + armedBandit.getRewardDistribution().getStandardDeviation()) {
                obsCenteredValues++;
            }

            i++;
        }

        assertTrue((double) obsCenteredValues / N < 0.69);
    }

    @Test
    void EstimateShouldConvergeTowardDistributionMean() {

        ArmedBandit armedBandit = new ArmedBandit(new GaussianDistribution(3, 5));

        int i = 0;
        int N = 1000;
        while (i < N) {
            armedBandit.performAction();
            i++;
        }

        assertEquals(N, armedBandit.getActionValueEstimate().getStepSize());
        assertEquals(armedBandit.getRewardDistribution().getMean(), Math.round(armedBandit.getActionValueEstimate().getValue()));
    }

    @Test
    public void performOneStep() {
        int k = 5;
        BanditProblem banditProblem = Utils.createValueBasedBanditProblem(k, new EpsilonGreedy(0.1));

        assertEquals(k, banditProblem.getArmedBandits().size());

        Reward reward = banditProblem.performOneStep();
        assertNotNull(reward);

        Optional<ArmedBandit> bandit = banditProblem.getArmedBandits()
                .stream()
                .filter(ArmedBandit -> ArmedBandit.getActionValueEstimate().getStepSize() == 1)
                .findFirst();

        assertTrue(bandit.isPresent());
        assertEquals(reward.getValue(), bandit.get().getActionValueEstimate().getValue());

        //TODO use hamcrest
        List<ArmedBandit> unexploredBandits = banditProblem.getArmedBandits()
                .stream()
                .filter(ArmedBandit -> !ArmedBandit.equals(bandit.get()))
                .filter(ArmedBandit -> ArmedBandit.getActionValueEstimate().getStepSize() == 0 && ArmedBandit.getActionValueEstimate().getValue() == 0)
                .collect(Collectors.toList());

        assertEquals(k - 1, unexploredBandits.size());
    }

    @Test
    public void ShouldFollowNbTotalOfStepsDistributedInAllBandits() {
        int k = 5;
        BanditProblem banditProblem = Utils.createValueBasedBanditProblem(k, new EpsilonGreedy(0.1));

        int totalStepCountExpected = 1000;
        int i = 0;
        while (i < totalStepCountExpected) {
            banditProblem.performOneStep();
            i++;
        }

        int actualStepCount = banditProblem.getArmedBandits().stream()
                .map(ArmedBandit -> ArmedBandit.getActionValueEstimate().getStepSize())
                .mapToInt(Integer::intValue)
                .sum();

        assertEquals(totalStepCountExpected, actualStepCount);
    }

}
