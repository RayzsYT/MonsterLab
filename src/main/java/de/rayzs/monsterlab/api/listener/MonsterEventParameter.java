package de.rayzs.monsterlab.api.listener;

import de.rayzs.monsterlab.api.monster.Monster;
import org.bukkit.entity.Entity;

public class MonsterEventParameter {

    private Entity damager = null;
    private Monster monster = null;

    public static MonsterEventParameter createParameter(final Monster monster) {
        return new MonsterEventParameter().setMonster(monster);
    }

    public static MonsterEventParameter createParameter(final Entity damager) {
        return new MonsterEventParameter().setDamager(damager);
    }

    public static MonsterEventParameter createParameter(final Monster monster, final Entity damager) {
        return new MonsterEventParameter().setMonster(monster).setDamager(damager);
    }

    public MonsterEventParameter setDamager(final Entity damager) {
        this.damager = damager;
        return this;
    }

    public MonsterEventParameter setMonster(final Monster monster) {
        this.monster = monster;
        return this;
    }

    public Entity getDamager() {
        return damager;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean hasDamager() {
        return damager != null;
    }

    public boolean hasMonster() {
        return monster != null;
    }
}
