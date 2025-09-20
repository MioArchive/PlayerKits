package net.javamio.playerkits.data.kitroom;

import lombok.Getter;
import org.bukkit.Material;

import java.awt.*;

@Getter
public enum KitRoomCategory {

    CRYSTAL_PVP("crystal", Material.END_CRYSTAL, "&#579af7Crystal PVP", 47),
    POTIONS("potions", Material.SPLASH_POTION, "&#579af7Potions", 48),
    CONSUMABLES("consumables", Material.ENDER_PEARL, "&#579af7Consumables", 49),
    ARROWS("arrows", Material.ARROW, "&#579af7Arrows", 50),
    MISCELLANEOUS("misc", Material.AXOLOTL_BUCKET, "&#579af7Miscellaneous", 51);

    @Getter
    private final String path;
    private final Material type;
    private final Component displayName;
    private final int slot;

    KitRoomCategory(String path, Material type, Component displayName, int slot) {
        this.path = path;
        this.type = type;
        this.displayName = displayName;
        this.slot = slot;
    }
}
