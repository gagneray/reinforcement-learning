package org.gagneray.rl.common.distributions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GaussianDistribution {

    private final Double standardDeviation;
    private final Random random = new Random();
    private Double mean;
    private Double variance;

    public GaussianDistribution(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
        this.standardDeviation = Math.sqrt(variance);
    }

    public static List<GaussianDistribution> generateDefaultDistributions(int k) {
        GaussianDistribution random = new GaussianDistribution(0, 1);
        List<GaussianDistribution> gaussianDistributions = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            gaussianDistributions.add(new GaussianDistribution(random.generateValue(), 1));
        }

        return gaussianDistributions;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GaussianDistribution that = (GaussianDistribution) o;
        return Double.compare(that.mean, mean) == 0 &&
                Double.compare(that.variance, variance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mean, variance);
    }

    @Override
    public String toString() {
        return "GaussianDistribution{" +
                "mean=" + mean +
                ", variance=" + variance +
                '}';
    }

    public double generateValue() {
        return mean + random.nextGaussian() * standardDeviation;
    }
}
