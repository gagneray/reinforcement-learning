package org.gagneray.rl.banditproblem.factories;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.gagneray.rl.banditproblem.actionSelectionPolicies.ActionPolicyNames;
import org.gagneray.rl.banditproblem.actionSelectionPolicies.EpsilonGreedy;
import org.gagneray.rl.banditproblem.actionSelectionPolicies.UpperConfidenceBound;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.policies.ActionSectionPolicy;

public class ActionSelectionFactory {

    public static final String POLICY_NAME = "name";
    private static final String EPSILON = "epsilon";
    private static final String C = "c";

    public ActionSectionPolicy<ArmedBandit> getPolicy(JsonNode jsonNode) {

        String policyName = String.valueOf(jsonNode.get(POLICY_NAME).asText());
        if (StringUtils.isEmpty(policyName)) {
            throw new IllegalArgumentException("unable to get policy name key from json " + jsonNode);
        }

        switch (ActionPolicyNames.valueOf(policyName)) {
            case E_GREEDY:
                return getEpsilonGreedy(jsonNode);
            case UCB:
                return getUpperConfidenceBound(jsonNode);
            default:
                throw new IllegalStateException("Unexpected policy name value : " + ActionPolicyNames.valueOf(policyName));
        }
    }

    private UpperConfidenceBound getUpperConfidenceBound(JsonNode jsonNode) {
        String c = String.valueOf(jsonNode.get(C));
        if (StringUtils.isEmpty(c)) {
            throw new IllegalArgumentException("unable to get confidence parameter from json " + jsonNode);
        }
        return new UpperConfidenceBound(Double.parseDouble(c));
    }

    private EpsilonGreedy getEpsilonGreedy(JsonNode jsonNode) {
        String epsilon = String.valueOf(jsonNode.get(EPSILON));
        if (StringUtils.isEmpty(epsilon)) {
            throw new IllegalArgumentException("unable to get epsilon greedy parameter from json " + jsonNode);
        }
        return new EpsilonGreedy(Double.parseDouble(epsilon));
    }
}
