package org.gagneray.rl.banditproblem.actionValueEstimates;

import org.gagneray.rl.common.distributions.GaussianDistribution;
import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionValueEstimateTest {


    @Test
    public void valueEstimateIncremental() {
        // sample average method should return the same estimate when it is computed using all the values in memory and using the incremental method
        ActionValueEstimate estimate = new ActionValueEstimate();
        GaussianDistribution distribution = new GaussianDistribution(0, 1);
        List<Double> values = new ArrayList<>();
        int n = 1000;
        double sum = 0;

        // Use Number format to handle double precision error
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(6);
        for (int i = 0; i < n; i++) {
            double generatedValue = distribution.generateValue();
            values.add(generatedValue);
            sum += generatedValue;
            estimate.next(generatedValue);
            double mean = sum / (i + 1);

            assertEquals(numberFormat.format(mean), numberFormat.format(estimate.getValue()));
        }
    }

}