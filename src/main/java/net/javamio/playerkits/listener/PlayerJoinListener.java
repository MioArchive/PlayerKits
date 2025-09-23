package net.javamio.playerkits.listener;

import net.javamio.playerkits.data.cache.PlayerDataCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        PlayerDataCache.loadPlayer(player.getUniqueId());
    }
}
