package net.javamio.playerkits.data.cache;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerKitData {

    @Getter
    private final UUID playerUUID;
    private final Map<Integer, Map<Integer, ItemStack>> cachedKits;
    private final Map<Integer, Long> kitLoadTimes;
    private final long loadTime;

    public PlayerKitData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.cachedKits = new ConcurrentHashMap<>();
        this.kitLoadTimes = new ConcurrentHashMap<>();
        this.loadTime = System.currentTimeMillis();
    }

    public void cacheKit(int kitNumber, Map<Integer, ItemStack> contents) {
        if (contents == null) {
            contents = new HashMap<>();
        }

        Map<Integer, ItemStack> clonedContents = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> entry : contents.entrySet()) {
            ItemStack item = entry.getValue();
            if (item != null) {
                clonedContents.put(entry.getKey(), item.clone());
            }
        }

        cachedKits.put(kitNumber, clonedContents);
        kitLoadTimes.put(kitNumber, System.currentTimeMillis());
    }

    public Map<Integer, ItemStack> getCachedKit(int kitNumber) {
        Map<Integer, ItemStack> kit = cachedKits.get(kitNumber);
        if (kit == null) {
            return new HashMap<>();
        }

        Map<Integer, ItemStack> clonedKit = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> entry : kit.entrySet()) {
            ItemStack item = entry.getValue();
            if (item != null) {
                clonedKit.put(entry.getKey(), item.clone());
            }
        }

        return clonedKit;
    }

    public boolean hasKit(int kitNumber) {
        return cachedKits.containsKey(kitNumber) && !cachedKits.get(kitNumber).isEmpty();
    }

    public void invalidateKit(int kitNumber) {
        cachedKits.remove(kitNumber);
        kitLoadTimes.remove(kitNumber);
    }

    public void invalidateAllKits() {
        cachedKits.clear();
        kitLoadTimes.clear();
    }

    public boolean isKitCached(int kitNumber) {
        return cachedKits.containsKey(kitNumber);
    }

    public long getKitLoadTime(int kitNumber) {
        return kitLoadTimes.getOrDefault(kitNumber, 0L);
    }

}
