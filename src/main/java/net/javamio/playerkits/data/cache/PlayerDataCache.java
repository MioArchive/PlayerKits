package net.javamio.playerkits.data.cache;

import lombok.experimental.UtilityClass;
import net.javamio.playerkits.PlayerKits;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class PlayerDataCache {

    private final Map<UUID, PlayerKitData> cache = new ConcurrentHashMap<>();

    public void loadPlayer(UUID playerUUID) {
        if (cache.containsKey(playerUUID)) {
            return;
        }

        PlayerKitData data = new PlayerKitData(playerUUID);
        cache.put(playerUUID, data);

        PlayerKits.LOGGER.info("Loaded data for player: " + playerUUID);
    }

    public void unloadPlayer(UUID playerUUID) {
        PlayerKitData data = cache.remove(playerUUID);
        if (data != null) {
            PlayerKits.LOGGER.info("Unloaded data for player: " + playerUUID);
        }
    }

    public PlayerKitData getPlayer(UUID playerUUID) {
        return cache.get(playerUUID);
    }

    public boolean isPlayerLoaded(UUID playerUUID) {
        return cache.containsKey(playerUUID);
    }

    public void cacheKit(UUID playerUUID, int kitNumber, Map<Integer, ItemStack> contents) {
        PlayerKitData data = getPlayer(playerUUID);
        if (data != null) {
            data.cacheKit(kitNumber, contents);
        }
    }

    public Map<Integer, ItemStack> getCachedKit(UUID playerUUID, int kitNumber) {
        PlayerKitData data = getPlayer(playerUUID);
        if (data != null) {
            return data.getCachedKit(kitNumber);
        }
        return new HashMap<>();
    }

    public boolean hasKitCached(UUID playerUUID, int kitNumber) {
        PlayerKitData data = getPlayer(playerUUID);
        if (data != null) {
            return data.hasKit(kitNumber);
        }
        return false;
    }

    public void invalidateKit(UUID playerUUID, int kitNumber) {
        PlayerKitData data = getPlayer(playerUUID);
        if (data != null) {
            data.invalidateKit(kitNumber);
        }
    }

    public void clearCache() {
        cache.clear();
        PlayerKits.LOGGER.info("Cleared all player data cache");
    }

    public int getCacheSize() {
        return cache.size();
    }
}