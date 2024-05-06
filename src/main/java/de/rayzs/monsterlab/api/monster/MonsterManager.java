package de.rayzs.monsterlab.api.monster;

import org.bukkit.entity.Entity;
import java.util.*;

public class MonsterManager {

    private static final List<Monster> MONSTERS = new ArrayList<>();

    /**
     * Add a new monster
     *
     * @param monster The monster which should be added
     * @return Boolean if monster could be added into the list or not
     */
    public static boolean add(final Monster monster) {
        return MONSTERS.add(monster);
    }

    /**
     * Remove a monster from the list
     *
     * @param monster The monster which should be removed
     * @return Boolean if monster could be removed from the list or not
     */
    public static boolean remove(final Monster monster) {
        return MONSTERS.remove(monster);
    }

    /**
     * Find a monster through the list using the entity
     *
     * @param entity The target entity
     * @return The monster
     */
    public static Monster getMonsterByEntity(Entity entity) {
        Optional<Monster> optional = MONSTERS.stream().filter(monster -> monster.getEntity() == entity).findFirst();
        return optional.orElse(null);
    }

    /**
     * Find out if the target entity belongs to a monster's hologram
     *
     * @param entity The target entity
     * @return The monster
     */
    public static Monster getMonsterByHologramEntity(Entity entity) {
        Optional<Monster> optional = MONSTERS.stream().filter(monster -> monster.getHologram() == entity).findFirst();
        return optional.orElse(null);
    }

    /**
     * Check if target entity is a monster or not
     *
     * @param entity The target entity
     * @return If entity is a monster or not
     */
    public static boolean isMonster(Entity entity) {
        return getMonsterByEntity(entity) != null;
    }

    /**
     * Check if target entity is a hologram monster if a monster
     *
     * @param entity The target entity
     * @return If entity is hologram of a monster or not
     */
    public static boolean isMonstersHologram(Entity entity) {
        return getMonsterByHologramEntity(entity) != null;
    }

    /**
     * Remove/Kill all monsters who are still alive
     */
    public static void removeAll() {
        Monster monster;

        for(int i = 0; i < MONSTERS.size(); i++) {
            monster = MONSTERS.get(i);
            monster.remove();
        }

        MONSTERS.clear();
    }
}
