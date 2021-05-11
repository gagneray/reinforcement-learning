package org.gagneray.rl.common.entities;

import java.util.Objects;

public abstract class Entity {

    private static int count = 0;

    // id is mostly used for getting different Hashcode of the class instantiation
    public final Integer id;

    public Entity() {
        this.id = count++;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
