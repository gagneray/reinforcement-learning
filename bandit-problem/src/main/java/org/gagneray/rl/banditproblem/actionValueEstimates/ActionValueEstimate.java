package org.gagneray.rl.banditproblem.actionValueEstimates;


import org.gagneray.rl.common.distributions.EstimateUtils;

import java.util.Objects;

public class ActionValueEstimate {

    private int stepSize;
    private double value;

    public ActionValueEstimate() {
        this.stepSize = 0;
        this.value = 0;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Compute the next estimated value being the average of all values received using the incremental method
     *
     * @param value NewEstimate = OldEstimate + StepSize * [ Target - OldEstimate ]
     *              Target being the reward value received
     */
    public void next(double value) {
        this.setStepSize(this.getStepSize() + 1);
        this.setValue(EstimateUtils.computeSampleAverageIncremental(this.value, this.stepSize, value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionValueEstimate that = (ActionValueEstimate) o;
        return stepSize == that.stepSize && Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepSize, value);
    }

    @Override
    public String toString() {
        return "ActionValueEstimate{" +
                "stepSize=" + stepSize +
                ", value=" + value +
                '}';
    }
}
