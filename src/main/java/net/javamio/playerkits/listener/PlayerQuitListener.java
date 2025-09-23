package net.javamio.playerkits.listener;

import net.javamio.playerkits.data.cache.PlayerDataCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        PlayerDataCache.unloadPlayer(player.getUniqueId());
    }

}
