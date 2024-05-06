package de.rayzs.monsterlab.plugin.events.monster;

import de.rayzs.monsterlab.api.monster.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.*;

public class EntityCombust implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityCombust(final EntityCombustEvent event) {
        if(event instanceof EntityCombustByEntityEvent || event instanceof EntityCombustByBlockEvent) return;

        final Entity entity = event.getEntity();
        final Monster monster = MonsterManager.getMonsterByEntity(entity);

        event.setCancelled(monster != null);
    }
}
