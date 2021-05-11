package utils;

import org.gagneray.rl.banditproblem.actionSelectionPolicies.ValueBasedActionSelectionPolicy;
import org.gagneray.rl.banditproblem.entities.ArmedBandit;
import org.gagneray.rl.banditproblem.entities.BanditProblem;
import org.gagneray.rl.common.distributions.GaussianDistribution;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static BanditProblem createValueBasedBanditProblem(int k, ValueBasedActionSelectionPolicy policy) {


        GaussianDistribution g = new GaussianDistribution(0, 1);
        List<ArmedBandit> armedBandits = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            armedBandits.add(new ArmedBandit(new GaussianDistribution(0, 1)));
        }

        return new BanditProblem(armedBandits, policy);
    }

}
