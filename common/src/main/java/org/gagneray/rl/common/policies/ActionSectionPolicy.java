package org.gagneray.rl.common.policies;

import org.gagneray.rl.common.entities.Entity;

import java.util.List;

public abstract class ActionSectionPolicy<K extends Entity> {
    public abstract K selectAction(List<K> elements);
}
