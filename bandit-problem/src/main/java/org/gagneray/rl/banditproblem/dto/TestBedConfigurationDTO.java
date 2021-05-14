package org.gagneray.rl.banditproblem.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.banditproblem.factories.ActionSelectionFactory;
import org.gagneray.rl.common.policies.ActionSectionPolicy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(using = TestBedConfigurationDTO.TestBedConfigurationDTODeserializer.class)
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

    public static class TestBedConfigurationDTODeserializer extends StdDeserializer<TestBedConfigurationDTO> {

        public static final String ACTION_POLICIES_KEY = "policies";
        public static final String BANDIT_PROBLEM_COUNT_KEY = "banditProblemCount";
        public static final String BANDIT_PER_PROBLEM_KEY = "k";
        public static final String TOTAL_STEPS_KEY = "totalSteps";

        public TestBedConfigurationDTODeserializer() {
            this(null);
        }

        protected TestBedConfigurationDTODeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public TestBedConfigurationDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            TestBedConfigurationDTO testBedConfigurationDTO = new TestBedConfigurationDTO();

            if (!Objects.requireNonNull(node).get(ACTION_POLICIES_KEY).isArray()) {
                throw new IllegalArgumentException("Couldn't read policies from json array");
            }

            ActionSelectionFactory actionSelectionFactory = new ActionSelectionFactory();

            Objects.requireNonNull(node).get(ACTION_POLICIES_KEY)
                    .forEach(jsonNode -> testBedConfigurationDTO.getPolicies().add(actionSelectionFactory.getPolicy(jsonNode)));

            testBedConfigurationDTO.setBanditProblemCount(Objects.requireNonNull(node).get(BANDIT_PROBLEM_COUNT_KEY).asInt());
            testBedConfigurationDTO.setK(Objects.requireNonNull(node).get(BANDIT_PER_PROBLEM_KEY).asInt());
            testBedConfigurationDTO.setTotalSteps(Objects.requireNonNull(node).get(TOTAL_STEPS_KEY).asInt());

            return testBedConfigurationDTO;
        }
    }
}
