package org.gagneray.rl.banditproblem.actionSelectionPolicies;

import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.banditproblem.entities.BanditProblem;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpperConfidenceBoundTest {

    @Test
    void shouldTestAllBanditsAtLeastOnce() {

        BanditProblem banditProblem = Utils.createValueBasedBanditProblem(5, new UpperConfidenceBound(2));

        int N = banditProblem.getArmedBandits().size();
        int i = 0;

        while (i < N) {
            banditProblem.performOneStep();
            i++;
        }

        for (ArmedBandit bandit : banditProblem.getArmedBandits()) {
            assertEquals(1, bandit.getActionValueEstimate().getStepSize());
        }

    }

    @Test
    void CompareShouldReturnHighestValueWhenSameStepSize() {

        double value = 2;
        int step = 1;
        ArmedBandit bandit1 = new ArmedBandit();
        bandit1.getActionValueEstimate().setValue(value);
        bandit1.getActionValueEstimate().setStepSize(step);

        ArmedBandit bandit2 = new ArmedBandit();
        bandit1.getActionValueEstimate().setValue(value + 1);
        bandit1.getActionValueEstimate().setStepSize(step);

        List<ArmedBandit> list = new ArrayList<>();
        list.add(bandit1);
        list.add(bandit2);

        UpperConfidenceBound ucb = new UpperConfidenceBound(2);

        ArmedBandit selectedBandit = ucb.selectAction(list);

        assertEquals(bandit2, selectedBandit);
    }

    @Test
    void CompareShouldReturnLessExploredWhenSameValues() {

        double value = 2;
        int step = 1;
        ArmedBandit bandit1 = new ArmedBandit();
        bandit1.getActionValueEstimate().setValue(value);
        bandit1.getActionValueEstimate().setStepSize(step + 1);

        ArmedBandit bandit2 = new ArmedBandit();
        bandit1.getActionValueEstimate().setValue(value);
        bandit1.getActionValueEstimate().setStepSize(step);

        List<ArmedBandit> list = new ArrayList<>();
        list.add(bandit1);
        list.add(bandit2);

        UpperConfidenceBound ucb = new UpperConfidenceBound(2);

        ArmedBandit selectedBandit = ucb.selectAction(list);

        assertEquals(bandit2, selectedBandit);
    }
}