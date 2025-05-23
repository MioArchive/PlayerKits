package net.javamio.playerkits.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class KitHolder {

    private final UUID uuid;
    private final Map<Integer, Kit> kits;

    public static KitHolder newEmpty(UUID uuid) {
        Map<Integer, Kit> initializedKits = createEmptyKits(uuid);
        return new KitHolder(uuid, initializedKits);
    }

    private static Map<Integer, Kit> createEmptyKits(UUID uuid) {
        Map<Integer, Kit> kits = new HashMap<>();
        for (int slot = 1; slot <= 16; slot++) {
            kits.put(slot, new Kit(uuid, slot, new HashMap<>()));
        }
        return kits;
    }
}
