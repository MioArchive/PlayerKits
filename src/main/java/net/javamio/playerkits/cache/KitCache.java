package net.javamio.playerkits.cache;

import lombok.experimental.UtilityClass;
import net.javamio.playerkits.object.Kit;
import net.javamio.playerkits.object.KitHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class KitCache {
    private final Map<UUID, KitHolder> kits = new ConcurrentHashMap<>();

    public void addKit(UUID playerUUID, Kit kit) {
        kits.computeIfAbsent(playerUUID, KitHolder::newEmpty);
        KitHolder holder = kits.get(playerUUID);
        holder.getKits().remove(kit.getId());
        holder.getKits().put(kit.getId(), kit);
    }

    public void removeKit(UUID playerUUID, Kit item) {
        kits.computeIfPresent(playerUUID, (uuid, items) -> {
            items.getKits().remove(item.getId());
            items.getKits().put(item.getId(), new Kit(uuid, item.getId(), new HashMap<>()));
            return items;
        });
    }

    public void update(UUID playerUUID, KitHolder holder) {
        kits.put(playerUUID, holder);
    }

    public void invalidate(UUID playerUUID) {
        kits.remove(playerUUID);
    }

    public Map<Integer, Kit> getKits(UUID playerUUID) {
        if (kits.get(playerUUID) == null) {
            return new HashMap<>();
        }
        Map<Integer, Kit> ret = new HashMap<>();
        kits.computeIfPresent(playerUUID, (uuid, items) -> {
            ret.putAll(items.getKits());
            return items;
        });
        return ret;
    }
}