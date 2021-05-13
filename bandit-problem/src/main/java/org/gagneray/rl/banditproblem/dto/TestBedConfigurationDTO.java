package org.gagneray.rl.banditproblem.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.policies.ActionSectionPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(using = TestBedConfigurationDTODeserializer.class)
public class TestBedConfigurationDTO {

    private List<ActionSectionPolicy<ArmedBandit>> policies = new ArrayList<>();
    private int banditProblemCount;
    private int k;
    private int totalSteps;

    public TestBedConfigurationDTO() {
    }

    public List<ActionSectionPolicy<ArmedBandit>> getPolicies() {
        return policies;
    }

    public void setPolicies(List<ActionSectionPolicy<ArmedBandit>> policies) {
        this.policies = policies;
    }

    public int getBanditProblemCount() {
        return banditProblemCount;
    }

    public void setBanditProblemCount(int banditProblemCount) {
        this.banditProblemCount = banditProblemCount;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBedConfigurationDTO that = (TestBedConfigurationDTO) o;
        return banditProblemCount == that.banditProblemCount && k == that.k && totalSteps == that.totalSteps && Objects.equals(policies, that.policies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policies, banditProblemCount, k, totalSteps);
    }

    @Override
    public String toString() {
        return "TestBedConfigurationDTO{" +
                "policies=" + policies +
                ", banditProblemCount=" + banditProblemCount +
                ", k=" + k +
                ", totalSteps=" + totalSteps +
                '}';
    }
}
