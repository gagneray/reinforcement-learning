package org.gagneray.rl.banditproblem.actionSelectionPolicies;

import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EpsilonGreedy extends ValueBasedActionSelectionPolicy {

    private final static Logger LOGGER = LoggerFactory.getLogger(EpsilonGreedy.class);

    private final Double epsilon;
    private final Random random = new Random();

    public EpsilonGreedy(double epsilon) {

        LOGGER.debug("Creating e-greedy policy, e={}", epsilon);

        if (epsilon < 0 || epsilon > 1) {
            throw new IllegalArgumentException("Epsilon value must be set in a bounded interval [0 , 1], invalid value : " + epsilon);
        }

        this.epsilon = epsilon;
    }

    public ActionType applyPolicy() {

        if (random.nextDouble() >= 1 - epsilon) {
            return ActionType.EXPLORATION;
        } else {
            return ActionType.EXPLOITATION;
        }
    }

    private ArmedBandit explore(List<ArmedBandit> elements) {
        if (elements.size() <= 1) {
            throw new IllegalStateException("No element to explore, collection of elements is too small (size = " + elements.size());
        }

        return elements.subList(1, elements.size())
                .get(random.nextInt(elements.size() - 1));
    }

    @Override
    public ArmedBandit selectAction(List<ArmedBandit> bandits) {
        ActionType actionType = this.applyPolicy();
        bandits.sort(this);

        LOGGER.debug("Select action {}", actionType);

        switch (actionType) {
            case EXPLOITATION:
                return bandits.get(0);
            case EXPLORATION:
                return explore(bandits);
            default:
                throw new IllegalStateException("Unexpected value: " + actionType);
        }
    }


    @Override
    public int compare(ArmedBandit o1, ArmedBandit o2) {
        int result = 0;

        if (o2.getActionValueEstimate().getValue() < o1.getActionValueEstimate().getValue()) {
            result = -1;
        } else if (o2.getActionValueEstimate().getValue() > o1.getActionValueEstimate().getValue()) {
            result = 1;
        }

        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpsilonGreedy that = (EpsilonGreedy) o;
        return Objects.equals(epsilon, that.epsilon) && Objects.equals(random, that.random);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epsilon, random);
    }

    @Override
    public String toString() {
        return "EpsilonGreedyPolicy{" +
                "epsilon=" + epsilon +
                '}';
    }


    public enum ActionType {
        EXPLORATION,
        EXPLOITATION;
    }
}
