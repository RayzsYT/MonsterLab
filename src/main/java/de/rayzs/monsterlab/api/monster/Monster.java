package de.rayzs.monsterlab.api.monster;

import de.rayzs.monsterlab.api.monster.attack.Attack;
import de.rayzs.monsterlab.api.listener.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.*;

public class Monster {

    private List<Attack> attacks;

    private Entity entity;
    private ArmorStand hologram;
    private MonsterAttackType attackType;

    private String name;
    private long maxHealth, maxEnergy, health, energy, attackCooldown, lastAttackTime;

    private boolean alive = false, cancelDefaultDamage = false;

    /**
     * Create a monster-class & skips the whole builder-part
     *
     * @param health Maximum health of the monster
     * @param energy Maximum capacity of energy to execute attacks
     * @param name Name of the monster (over head)
     * @param attackType Decides what entity it's gonna be (Skeleton/Zombie)
     * @param cancelDefaultDamage Cancel the default entity-damage animation & knockback
     * @param attackCooldown The cooldown until the monster can launch the next attack
     * @param attacks The default attacks the monster can choose from
     * @return Monster-Class and skipped whole builder with it
     */
    public static Monster createMonster(final long health,
                                        final long energy,
                                        final String name,
                                        final MonsterAttackType attackType,
                                        final boolean cancelDefaultDamage,
                                        final long attackCooldown,
                                        final Attack... attacks) {

        return new Monster()
                .setMaxHealth(health)
                .setMaxEnergy(energy)
                .setHealth(health)
                .setEnergy(energy)
                .setDefaultDamageCancelled(cancelDefaultDamage)
                .setAttackCooldown(attackCooldown)
                .setName(name)
                .setAttacks(attacks)
                .setAttackType(attackType);
    }

    /**
     * Create a monster-class & skips the whole builder-part
     *
     * @param health Maximum health of the monster
     * @param energy Maximum capacity of energy to execute attacks
     * @param name Name of the monster (over head)
     * @param attackType Decides what entity it's gonna be (Skeleton/Zombie)
     * @param cancelDefaultDamage Cancel the default entity-damage animation & knockback
     * @param attackCooldown The cooldown until the monster can launch the next attack
     * @return Monster-Class and skipped whole builder with it
     */
    public static Monster createMonster(final long health,
                                        final long energy,
                                        final String name,
                                        final MonsterAttackType attackType,
                                        final boolean cancelDefaultDamage,
                                        final long attackCooldown) {

        return new Monster()
                .setMaxHealth(health)
                .setMaxEnergy(energy)
                .setHealth(health)
                .setEnergy(energy)
                .setDefaultDamageCancelled(cancelDefaultDamage)
                .setAttackCooldown(attackCooldown)
                .setName(name)
                .setAttackType(attackType);
    }

    public Monster setDefaultDamageCancelled(final boolean cancelDefaultDamage) {
        this.cancelDefaultDamage = cancelDefaultDamage;
        return this;
    }

    public Monster setMaxHealth(final long maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public Monster setMaxEnergy(final long maxEnergy) {
        this.maxEnergy = maxEnergy;
        return this;
    }

    public Monster setHealth(final long health) {
        this.health = health;
        return this;
    }

    public Monster setEnergy(final long energy) {
        this.energy = energy;
        return this;
    }

    public Monster setAttackCooldown(final long attackCooldown) {
        this.attackCooldown = attackCooldown;
        return this;
    }

    public Monster setName(final String name) {
        this.name = name;
        return this;
    }

    public Monster setAttackType(final MonsterAttackType attackType) {
        this.attackType = attackType;
        return this;
    }

    public Monster setAttacks(final Attack... attacks) {
        setAttacks(Arrays.asList(attacks));
        return this;
    }

    public Monster setAttacks(final List<Attack> attacks) {
        this.attacks = attacks;
        return this;
    }

    public Monster addAttack(final Attack attack) {
        this.attacks.add(attack);
        return this;
    }

    // Remove all attacks from the list
    public Monster clearAttacks() {
        this.attacks.clear();
        return this;
    }

    public Monster addAttack(final Attack... attacks) {
        for (Attack attack : attacks) addAttack(attack);
        return this;
    }

    /**
     * Launch a specific attack from the attack-list
     *
     * @param attack Attack which should be launched
     * @return If attack could be launched or not
     */
    public boolean launchAttack(final int attack) {
        if((attacks.size() - 1) > attack) return false;
        return launchAttack(attacks.get(attack));
    }

    /**
     * Launch a random attack from the attack-list
     *
     * @return If attack could be launched or not
     */
    public boolean launchRandomAttack() {
        return launchAttack(attacks.get(new Random().nextInt(attacks.size())));
    }

    /**
     * Launch a specific attack
     *
     * @param attack Attack which should be launched
     * @return If attack could be launched or not
     */
    public boolean launchAttack(final Attack attack) {
        final long currentAttackCooldown = System.currentTimeMillis() - this.lastAttackTime;
        if(currentAttackCooldown < this.attackCooldown || this.energy < attack.energyCost()) return false;

        this.lastAttackTime = System.currentTimeMillis();
        this.energy -= attack.energyCost();
        attack.execute(this);

        MonsterEventManager.call(
                MonsterEventType.ATTACK,
                MonsterEventParameter.createParameter(this)
        );

        return true;
    }

    /**
     * Spawn the monster
     *
     * @param location The location where the monster should spawn
     * @return Boolean if entity could be spawned or not
     */
    public boolean spawn(final Location location) {
        World world = location.getWorld();
        if(isAlive() || world == null) return false;

        this.alive = true;
        this.health = maxHealth;
        this.energy = maxEnergy;

        EntityType entityType = this.attackType == MonsterAttackType.SHORT_RANGE ? EntityType.ZOMBIE : EntityType.SKELETON;
        this.entity = world.spawnEntity(location, entityType);
        this.hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        MonsterManager.add(this);

        this.entity.setCustomNameVisible(false);
        this.hologram.setSmall(true);
        this.hologram.setGravity(false);
        this.hologram.setVisible(false);
        this.hologram.setCustomNameVisible(true);
        this.entity.setPassenger(this.hologram);

        updateHologram();

        MonsterEventManager.call(
                MonsterEventType.SPAWN,
                MonsterEventParameter.createParameter(this)
        );

        return true;
    }

    public void updateHologram() {
        this.hologram.setCustomName("§c§l" + name + " §8[§a§l" + health + "§8/§2§l" + maxHealth + "§8]");
    }

    public void receiveDamage(final Entity damager, final long damage) {
        this.health -= damage;

        MonsterEventManager.call(
                MonsterEventType.RECEIVE_DAMAGE,
                MonsterEventParameter.createParameter(this, damager)
        );

        if(this.health <= 0) die(damager);
        else updateHologram();
    }

    /**
     * When the monster dies without a slayer
     */
    public void die() { die(null); }

    /**
     * When the monster gets killed by an entity
     *
     * @param damager The killer
     */
    public void die(final Entity damager) {
        // final Location deathLocation = this.entity.getLocation();
        MonsterManager.remove(this);

        this.hologram.remove();
        this.entity.remove();
        this.health = 0;
        this.energy = 0;

        this.alive = false;

        /* - Flying armorstand which goes up slowly and says that the monster dies.
           - Currently not sure if it's really needed or not

        this.hologram = (ArmorStand) deathLocation.getWorld().spawnEntity(deathLocation, EntityType.ARMOR_STAND);
        this.hologram.setSmall(true);
        this.hologram.setGravity(true);
        this.hologram.setVisible(false);
        this.hologram.setCustomNameVisible(true);
        this.hologram.setCustomName("§4§l☠ §c§l" + name + " §cdied §4§l☠");
        this.hologram.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 1));
        this.hologram.teleport(deathLocation);

        Bukkit.getScheduler().scheduleSyncDelayedTask(MonsterLibPlugin.PLUGIN, () -> {
                this.hologram.remove();
                MonsterManager.remove(this);
        }, 40);
        */

        MonsterEventManager.call(
                MonsterEventType.DEATH,
                MonsterEventParameter.createParameter(this, damager)
        );
    }

    public void remove() {
        MonsterManager.remove(this);
        hologram.remove();
        entity.remove();
        health = 0;
        energy = 0;
        alive = false;
    }


public boolean isDefaultDamageCancelled() { return cancelDefaultDamage; }
    public String getName() { return name; }
    public Entity getEntity() { return entity; }
    public ArmorStand getHologram() { return hologram; }
    public List<Attack> getAttacks() { return attacks; }
    public long getEnergy() { return energy; }
    public long getHealth() { return health; }
    public long getAttackCooldown() { return attackCooldown; }
    public long getLastAttackTime() { return lastAttackTime; }
    public boolean isAlive() { return alive; }
    public boolean isDead() { return !alive; }
}
