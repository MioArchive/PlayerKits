package net.javamio.playerkits.data.kitroom;

import lombok.Getter;
import net.javamio.playerkits.util.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
public enum KitRoomCategory {

    CRYSTAL_PVP("crystal", Material.END_CRYSTAL, ColorUtil.translateColorCodes("&#579af7Crystal PVP"), 47),
    POTIONS("potions", Material.SPLASH_POTION, ColorUtil.translateColorCodes("&#579af7Potions"), 48),
    CONSUMABLES("consumables", Material.ENDER_PEARL, ColorUtil.translateColorCodes("&#579af7Consumables"), 49),
    ARROWS("arrows", Material.ARROW, ColorUtil.translateColorCodes("&#579af7Arrows"), 50),
    MISCELLANEOUS("misc", Material.AXOLOTL_BUCKET, ColorUtil.translateColorCodes("&#579af7Miscellaneous"), 51);

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

    public static List<String> getNames() {
        return Arrays.stream(KitRoomCategory.values())
                .map(KitRoomCategory::name)
                .toList();
    }
}
