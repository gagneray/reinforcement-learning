package org.gagneray.rl.banditproblem.entities;


import org.gagneray.rl.banditproblem.actionValueEstimates.ActionValueEstimate;
import org.gagneray.rl.common.distributions.GaussianDistribution;
import org.gagneray.rl.common.entities.Entity;
import org.gagneray.rl.common.rewards.Reward;

import java.util.Objects;

public class ArmedBandit extends Entity {

    // TODO generic distribution
    protected final GaussianDistribution rewardDistribution;
    private final ActionValueEstimate actionValueEstimate;

    public ArmedBandit() {
        super();
        this.rewardDistribution = new GaussianDistribution(0, 1);
        this.actionValueEstimate = new ActionValueEstimate();
    }

    public ArmedBandit(GaussianDistribution rewardDistribution) {
        this.rewardDistribution = rewardDistribution;
        this.actionValueEstimate = new ActionValueEstimate();
    }

    public GaussianDistribution getRewardDistribution() {
        return rewardDistribution;
    }

    public ActionValueEstimate getActionValueEstimate() {
        return actionValueEstimate;
    }

    public Reward performAction() {
        Reward reward = new Reward(rewardDistribution.generateValue());
        actionValueEstimate.next(reward.getValue());
        return reward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArmedBandit that = (ArmedBandit) o;
        return Objects.equals(rewardDistribution, that.rewardDistribution) && Objects.equals(actionValueEstimate, that.actionValueEstimate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rewardDistribution, actionValueEstimate);
    }

    @Override
    public String toString() {
        return "ArmedBandit{" +
                "rewardDistribution=" + rewardDistribution +
                ", actionValueEstimate=" + actionValueEstimate +
                ", id=" + id +
                '}';
    }
}