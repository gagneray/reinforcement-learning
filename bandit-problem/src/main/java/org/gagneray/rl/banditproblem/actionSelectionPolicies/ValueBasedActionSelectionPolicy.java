package org.gagneray.rl.banditproblem.actionSelectionPolicies;

import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.common.policies.ActionSectionPolicy;

import java.util.Comparator;

public abstract class ValueBasedActionSelectionPolicy extends ActionSectionPolicy<ArmedBandit> implements Comparator<ArmedBandit> {

}
