package de.rayzs.monsterlab.plugin.events.monster;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import de.rayzs.monsterlab.api.monster.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.*;

public class EntityDamage implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity(), damager = event.getDamager();
        final Monster monster = MonsterManager.getMonsterByEntity(entity);
        if(monster == null) return;

        monster.receiveDamage(damager, (long) event.getDamage());
        event.setDamage(0);
        event.setCancelled(monster.isDefaultDamageCancelled());
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onEntityDamage(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        final Monster monster = MonsterManager.getMonsterByEntity(entity);
        final String causeName = event.getCause().name();

        event.setCancelled(
                MonsterManager.isMonstersHologram(entity)
                || monster != null
                && !causeName.contains("ENTITY")
                && !causeName.contains("FIRE")
                && !causeName.contains("LAVA"));
    }
}
