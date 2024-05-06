package de.rayzs.monsterlab.plugin.events.monster;

import org.bukkit.event.entity.EntityDeathEvent;
import de.rayzs.monsterlab.api.monster.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.*;

public class EntityDeath implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDeathEvent(final EntityDeathEvent event) {
        final Entity entity = event.getEntity();
        final Monster monster = MonsterManager.getMonsterByEntity(entity);
        if(monster == null) return;

        event.setDroppedExp(0);
        event.getDrops().clear();
    }
}
