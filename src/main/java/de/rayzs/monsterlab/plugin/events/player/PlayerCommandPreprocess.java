package de.rayzs.monsterlab.plugin.events.player;

import de.rayzs.monsterlab.api.monster.Monster;
import de.rayzs.monsterlab.api.monster.MonsterAttackType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage().toLowerCase();

        if(!message.equals("/spawn")) return;
        event.setCancelled(true);

        player.sendMessage("Summoned");
        Monster monster = Monster.createMonster(100, 100, "Monster", MonsterAttackType.SHORT_RANGE, false, 5000);
        monster.spawn(player.getLocation());
    }
}
