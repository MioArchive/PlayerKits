package net.javamio.playerkits.data.playerkit.cache;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@RequiredArgsConstructor
public class PlayerKitData {

    private final @NotNull UUID playerUUID;
    private final @NotNull Map<Integer, Map<Integer, ItemStack>> cachedKits = new ConcurrentHashMap<>();
    private final @NotNull Map<Integer, Long> kitLoadTimes = new ConcurrentHashMap<>();
    private final long loadTime = System.currentTimeMillis();

    public void cacheKit(int kitNumber, @NotNull Map<Integer, ItemStack> contents) {
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

    public @NotNull Map<Integer, ItemStack> getCachedKit(int kitNumber) {
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