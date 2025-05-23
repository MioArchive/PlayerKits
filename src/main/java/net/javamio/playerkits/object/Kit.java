package net.javamio.playerkits.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor @Getter
public class Kit {

    private final UUID uuid;
    private final int id;
    private final Map<Integer, ItemStack> content;
}
