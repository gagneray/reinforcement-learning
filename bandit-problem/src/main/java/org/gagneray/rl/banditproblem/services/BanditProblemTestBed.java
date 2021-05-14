package org.gagneray.rl.banditproblem.services;

import org.gagneray.rl.banditproblem.configurations.BanditProblemConfiguration;
import org.gagneray.rl.banditproblem.configurations.TestBedConfiguration;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.banditproblem.entities.BanditProblem;
import org.gagneray.rl.common.distributions.EstimateUtils;
import org.gagneray.rl.common.rewards.Reward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smile.plot.swing.Canvas;
import smile.plot.swing.Line;
import smile.plot.swing.LinePlot;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BanditProblemTestBed {

    private final static Logger LOGGER = LoggerFactory.getLogger(BanditProblemTestBed.class);

    private final int totalSteps;
    private final Map<BanditProblemConfiguration, List<BanditProblem>> banditProblems;
    private final Map<BanditProblemConfiguration, List<Double>> meanAverages;
    private int stepCount;

    public BanditProblemTestBed(TestBedConfiguration testBedConfiguration) {
        LOGGER.info("Create BanditProblem TestBed from configuration");
        this.stepCount = 0;
        this.totalSteps = testBedConfiguration.getTotalSteps();

        banditProblems = new ConcurrentHashMap<>(testBedConfiguration.getBanditProblemConfigurations().size());
        meanAverages = new ConcurrentHashMap<>(testBedConfiguration.getBanditProblemConfigurations().size());
        testBedConfiguration.getBanditProblemConfigurations().parallelStream()
                .forEach(banditProblemConfiguration -> {
                    banditProblems.put(banditProblemConfiguration, createBanditProblems(testBedConfiguration, banditProblemConfiguration));
                    meanAverages.put(banditProblemConfiguration, new ArrayList<>(totalSteps));
                });

    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public Map<BanditProblemConfiguration, List<BanditProblem>> getBanditProblems() {
        return banditProblems;
    }

    public Map<BanditProblemConfiguration, List<Double>> getMeanAverages() {
        return meanAverages;
    }

    private List<BanditProblem> createBanditProblems(TestBedConfiguration testBedConfiguration, BanditProblemConfiguration banditProblemConfiguration) {
        LOGGER.info("Create {} BanditProblem from configuration with policy {}", testBedConfiguration.getBanditProblemCount(), banditProblemConfiguration.getActionSectionPolicy());
        List<BanditProblem> banditProblemList = new ArrayList<>(testBedConfiguration.getBanditProblemCount());
        for (int i = 0; i < testBedConfiguration.getBanditProblemCount(); i++) {
            // generate random distribution reward for every armedBandit in all bandit problems
            BanditProblemConfiguration generatedConf = new BanditProblemConfiguration(banditProblemConfiguration.getActionSectionPolicy(), banditProblemConfiguration.getK());
            banditProblemList.add(createBanditProblem(generatedConf));
        }
        return banditProblemList;
    }

    public BanditProblem createBanditProblem(BanditProblemConfiguration configuration) {
        List<ArmedBandit> armedBandits = new ArrayList<>(configuration.getK());

        configuration.getGaussianDistributions()
                .forEach(gaussianDistribution -> armedBandits.add(new ArmedBandit(gaussianDistribution)));

        return new BanditProblem(armedBandits, configuration.getActionSectionPolicy());
    }

    public void process() {

        LOGGER.info("Processing Bandit Problem TestBed");

        if (stepCount >= totalSteps) {
            throw new IllegalStateException("Test bed already processed");
        }

        while (stepCount < totalSteps) {
            processOneStep();
        }

        LOGGER.info("Bandit Problem TestBed done");
    }

    private void processOneStep() {

        banditProblems.entrySet().parallelStream()
                .forEach(entry -> {
                    double oneStepAverage = performOneStep(entry.getValue());
                    meanAverages.get(entry.getKey()).add(stepCount, oneStepAverage);
                });

        stepCount++;

        LOGGER.trace("Step Processed, step={}/{}", stepCount, totalSteps);
    }

    private double performOneStep(List<BanditProblem> banditProblemList) {
        double oneStepAverage = 0;
        int banditProblemCount = 0;
        for (BanditProblem banditProblem : banditProblemList) {
            Reward reward = banditProblem.performOneStep();
            banditProblemCount++;
            oneStepAverage = EstimateUtils.computeSampleAverageIncremental(oneStepAverage, banditProblemCount, reward.getValue());
        }

        return oneStepAverage;
    }

    public TestBedResultDTO getResults() {

        if (meanAverages.isEmpty()) {
            throw new IllegalStateException("No testbed Result available");
        }

        if (stepCount < totalSteps) {
            LOGGER.warn("Providing partial testbed results {}/{}", stepCount, totalSteps);
        }

        return new TestBedResultDTO(this);
    }

    public void plotMeanAverages() {

        LOGGER.info("Plotting mean average results on bandit testbed results");

        Canvas canvas = new Canvas(new double[meanAverages.size()], new double[meanAverages.size()], true);

        meanAverages.forEach((banditProblemConfiguration, meanRewards) -> {
            double[][] data = new double[totalSteps][2];

            for (int i = 0; i < totalSteps; i++) {
                data[i][0] = (double) i + 1;
                data[i][1] = meanRewards.get(i);
            }

            //generate random color
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);

            //generate legend
            String legend = banditProblemConfiguration.getActionSectionPolicy().toString();
            LinePlot linePlot = LinePlot.of(data, Line.Style.SOLID, randomColor, legend);
            canvas.add(linePlot);
        });

        canvas.setAxisLabel(0, "Steps");
        canvas.setAxisLabel(1, "Average Reward");

        JFrame jframe = null;
        try {
            jframe = canvas.window();
        } catch (InterruptedException | InvocationTargetException e) {
            LOGGER.error("Error while creating jframe : {}", e.getMessage());
        }
        assert jframe != null;
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanditProblemTestBed testBed = (BanditProblemTestBed) o;
        return totalSteps == testBed.totalSteps && stepCount == testBed.stepCount && Objects.equals(banditProblems, testBed.banditProblems) && Objects.equals(meanAverages, testBed.meanAverages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalSteps, banditProblems, meanAverages, stepCount);
    }

    @Override
    public String toString() {
        return "BanditProblemTestBed{" +
                "totalSteps=" + totalSteps +
                ", banditProblems=" + banditProblems +
                ", meanRewards=" + meanAverages +
                ", stepCount=" + stepCount +
                '}';
    }

    public static class TestBedResultDTO {
        private final Map<BanditProblemConfiguration, List<Double>> meanAverages;
        private final int totalSteps;

        public TestBedResultDTO(BanditProblemTestBed banditProblemTestBed) {
            this.meanAverages = banditProblemTestBed.getMeanAverages();
            this.totalSteps = banditProblemTestBed.getTotalSteps();
        }

        public Map<BanditProblemConfiguration, List<Double>> getMeanAverages() {
            return meanAverages;
        }

        public int getTotalSteps() {
            return totalSteps;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestBedResultDTO that = (TestBedResultDTO) o;
            return totalSteps == that.totalSteps && Objects.equals(meanAverages, that.meanAverages);
        }

        @Override
        public int hashCode() {
            return Objects.hash(meanAverages, totalSteps);
        }

        @Override
        public String toString() {
            return "TestBedResultDTO{" +
                    "result=" + meanAverages +
                    ", totalSteps=" + totalSteps +
                    '}';
        }
    }
}
