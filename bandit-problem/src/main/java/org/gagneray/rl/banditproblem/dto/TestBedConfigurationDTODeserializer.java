package org.gagneray.rl.banditproblem.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.gagneray.rl.banditproblem.factories.ActionSelectionFactory;

import java.io.IOException;
import java.util.Objects;

public class TestBedConfigurationDTODeserializer extends StdDeserializer<TestBedConfigurationDTO> {

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
