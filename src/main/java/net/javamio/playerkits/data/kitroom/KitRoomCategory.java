package net.javamio.playerkits.data.kitroom;

import lombok.Getter;
import net.javamio.playerkits.util.ColorUtil;
import org.bukkit.Material;

import java.awt.*;

@Getter
public enum KitRoomCategory {

    CRYSTAL_PVP("crystal", Material.END_CRYSTAL, (Component) ColorUtil.translateColorCodes("&#579af7Crystal PVP"), 47),
    POTIONS("potions", Material.SPLASH_POTION, (Component) ColorUtil.translateColorCodes("&#579af7Potions"), 48),
    CONSUMABLES("consumables", Material.ENDER_PEARL, (Component) ColorUtil.translateColorCodes("&#579af7Consumables"), 49),
    ARROWS("arrows", Material.ARROW, (Component) ColorUtil.translateColorCodes("&#579af7Arrows"), 50),
    MISCELLANEOUS("misc", Material.AXOLOTL_BUCKET, (Component) ColorUtil.translateColorCodes("&#579af7Miscellaneous"), 51);

    @Getter
    private final String path;
    private final Material material;
    private final Component displayName;
    private final int slot;

    KitRoomCategory(String path, Material material, Component displayName, int slot) {
        this.path = path;
        this.material = material;
        this.displayName = displayName;
        this.slot = slot;
    }
}
