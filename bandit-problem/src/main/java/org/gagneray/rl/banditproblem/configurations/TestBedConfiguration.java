package org.gagneray.rl.banditproblem.configurations;

import org.gagneray.rl.banditproblem.dto.TestBedConfigurationDTO;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.policies.ActionSectionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestBedConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestBedConfiguration.class);

    private final List<BanditProblemConfiguration> banditProblemConfigurations;
    private final int totalSteps;
    private final int banditProblemCount;

    public TestBedConfiguration(List<BanditProblemConfiguration> banditProblemConfigurations, int totalSteps, int banditProblemCount) {

        LOGGER.info("Create TestBed Configuration from configurations, total steps={}, banditProblemCount={}", totalSteps, banditProblemCount);

        this.banditProblemConfigurations = banditProblemConfigurations;
        this.totalSteps = totalSteps;
        this.banditProblemCount = banditProblemCount;
    }

    public TestBedConfiguration(List<ActionSectionPolicy<ArmedBandit>> policyList, int banditProblemCount, int k, int totalSteps) {

        LOGGER.info("Create TestBed Configuration from policies {}, total steps={}, banditProblemCount={}, k={}", policyList, totalSteps, banditProblemCount, k);

        this.totalSteps = totalSteps;
        this.banditProblemCount = banditProblemCount;

        List<BanditProblemConfiguration> banditProblemConfigurations = new ArrayList<>(policyList.size());
        policyList.forEach(policy -> {
            banditProblemConfigurations.add(new BanditProblemConfiguration(policy, k));
        });
        this.banditProblemConfigurations = banditProblemConfigurations;
    }

    public TestBedConfiguration(TestBedConfigurationDTO testBedConfigurationDTO) {
        this(testBedConfigurationDTO.getPolicies(), testBedConfigurationDTO.getBanditProblemCount(), testBedConfigurationDTO.getK(), testBedConfigurationDTO.getTotalSteps());
    }

    public List<BanditProblemConfiguration> getBanditProblemConfigurations() {
        return banditProblemConfigurations;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public int getBanditProblemCount() {
        return banditProblemCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBedConfiguration that = (TestBedConfiguration) o;
        return totalSteps == that.totalSteps && banditProblemCount == that.banditProblemCount && Objects.equals(banditProblemConfigurations, that.banditProblemConfigurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banditProblemConfigurations, totalSteps, banditProblemCount);
    }

    @Override
    public String toString() {
        return "TestBedConfiguration{" +
                "banditProblemConfigurations=" + banditProblemConfigurations +
                ", totalSteps=" + totalSteps +
                ", banditProblemCount=" + banditProblemCount +
                '}';
    }
}
