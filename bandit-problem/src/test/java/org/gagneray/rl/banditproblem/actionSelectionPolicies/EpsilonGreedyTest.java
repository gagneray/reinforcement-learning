package org.gagneray.rl.banditproblem.actionSelectionPolicies;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.EpsilonGreedy.ActionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpsilonGreedyTest {

    private static EpsilonGreedy egpolicy(double epsilon) {
        return new EpsilonGreedy(epsilon);
    }

    @Test
    void shouldAlwaysReturnExploitationWhenEpsilonIsZero() {
        ActionType actionType = egpolicy(0.0).applyPolicy();
        assertEquals(ActionType.EXPLOITATION, actionType);
    }

    @Test
    void shouldAlwaysReturnExploitationWhenEpsilonIsOne() {

        ActionType actionType = egpolicy(1.0).applyPolicy();
        assertEquals(ActionType.EXPLORATION, actionType);
    }

    @Test
    void shouldFollowExplorationRate() {

        double epsilon = 0.1;
        EpsilonGreedy actionSelectionPolicy = egpolicy(epsilon);

        int explorationCount = 0;
        int exploitationCount = 0;

        int N = 10000;
        int i = 0;

        while (i < N) {

            ActionType actionType = actionSelectionPolicy.applyPolicy();

            switch (actionType) {
                case EXPLORATION:
                    explorationCount++;
                    break;
                case EXPLOITATION:
                    exploitationCount++;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + actionType);
            }

            i++;
        }


        assertEquals(1 - epsilon, getRoundedProbability(exploitationCount, N));
        assertEquals(epsilon, getRoundedProbability(explorationCount, N));
    }

    private double getRoundedProbability(double actionTypeCountCount, int n) {
        return Math.round((actionTypeCountCount / n) * 10) / 10.0;
    }
}