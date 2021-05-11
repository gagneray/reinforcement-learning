package org.gagneray.rl.common.distributions;


public class EstimateUtils {


    /**
     * Compute the mean value of a sample using the incremental method
     * Incremental method prevent from
     * (1) storing all the sample values
     * (2) computing sum of sample in the numerator
     * Thus, this method allow to save memory and computing time
     *
     * @param currentMean the current mean of the sample
     * @param sampleSize  the sample size once newValue is added to the sample
     * @param newValue    the value added to the sample to average
     * @return the new sample average
     */
    public static double computeSampleAverageIncremental(double currentMean, int sampleSize, double newValue) {
        return currentMean + (newValue - currentMean) / sampleSize;
    }

}
