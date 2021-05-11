package org.gagneray.rl.banditproblem.actionSelectionPolicies;

import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class UpperConfidenceBound extends ValueBasedActionSelectionPolicy {

    private final static Logger LOGGER = LoggerFactory.getLogger(UpperConfidenceBound.class);

    private static int t = 0;
    private final Double c;

    public UpperConfidenceBound(double c) {

        LOGGER.debug("Creating UpperConfidenceBound policy, c={}", c);

        if (c <= 0) {
            throw new IllegalArgumentException("confidence level parameter must be positive, invalid value : " + c);
        }

        this.c = c;
    }


    @Override
    public ArmedBandit selectAction(List<ArmedBandit> bandits) {
        t++;
        bandits.sort(this);
        return bandits.get(0);
    }

    private Double computeOptimalPotential(ArmedBandit bandit) {
        return bandit.getActionValueEstimate().getValue() + c * Math.sqrt(Math.log(t) / bandit.getActionValueEstimate().getStepSize());
    }

    @Override
    public int compare(ArmedBandit o1, ArmedBandit o2) {
        return computeOptimalPotential(o2).compareTo(computeOptimalPotential(o1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpperConfidenceBound that = (UpperConfidenceBound) o;
        return Objects.equals(c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }

    @Override
    public String toString() {
        return "UpperConfidenceBound{" +
                "c=" + c +
                '}';
    }
}
