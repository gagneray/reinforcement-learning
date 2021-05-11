package org.gagneray.rl.banditproblem.entities;

import org.gagneray.rl.common.policies.ActionSectionPolicy;
import org.gagneray.rl.common.rewards.Reward;

import java.util.List;
import java.util.Objects;

public class BanditProblem {

    private final List<ArmedBandit> armedBandits;
    private final ActionSectionPolicy<ArmedBandit> actionSelectionPolicy;

    public BanditProblem(List<ArmedBandit> armedBandits, ActionSectionPolicy<ArmedBandit> actionSelectionPolicy) {
        this.armedBandits = armedBandits;
        this.actionSelectionPolicy = actionSelectionPolicy;
    }

    public List<ArmedBandit> getArmedBandits() {
        return armedBandits;
    }

    public ActionSectionPolicy<ArmedBandit> getActionSelectionPolicy() {
        return actionSelectionPolicy;
    }

    public ArmedBandit selectBanditForAction() {
        return actionSelectionPolicy.selectAction(armedBandits);
    }

    public Reward performOneStep() {
        ArmedBandit bandit = this.selectBanditForAction();
        return bandit.performAction();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanditProblem that = (BanditProblem) o;
        return Objects.equals(armedBandits, that.armedBandits) && Objects.equals(actionSelectionPolicy, that.actionSelectionPolicy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(armedBandits, actionSelectionPolicy);
    }

    @Override
    public String toString() {
        return "BanditProblem{" +
                "armedBandits=" + armedBandits +
                ", actionSelectionPolicy=" + actionSelectionPolicy +
                '}';
    }
}
