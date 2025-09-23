package net.javamio.playerkits.menu;

import net.javamio.playerkits.PlayerKits;
import net.javamio.playerkits.data.kitroom.KitRoomCategory;
import net.javamio.playerkits.util.ColorUtil;
import net.javamio.playerkits.util.inventory.InventoryBuilder;
import net.javamio.playerkits.util.inventory.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KitRoomMenu {

    private final InventoryBuilder inventory;
    private final KitRoomCategory category;

    public KitRoomMenu() {
        this(KitRoomCategory.CRYSTAL_PVP);
    }

    public KitRoomMenu(KitRoomCategory category) {
        this.category = category;
        this.inventory = new InventoryBuilder(54, ColorUtil.translateColorCodes("&#579af7Virtual Kit Room"), false);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        List<ItemStack> kitContents = PlayerKits.getInstance().getKitRoom().itemStacks(category);

        for (int i = 0; i < Math.min(kitContents.size(), 45); i++) {
            ItemStack item = kitContents.get(i);
            if (item != null && !item.getType().isAir()) {
                inventory.setItem(i, item.clone(), event -> event.setCancelled(false));
            }
        }

        List<Integer> border = List.of(45, 53);
        border.forEach(slot -> inventory.setItem(slot, ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE)
                .name(ColorUtil.translateColorCodes(" "))
                .build(), event -> event.setCancelled(true)));

        for (KitRoomCategory cat : KitRoomCategory.values()) {
            ItemBuilder categoryItem = ItemBuilder.item(cat.getMaterial())
                    .name((Component) cat.getDisplayName())
                    .addFlag(ItemFlag.HIDE_ITEM_SPECIFICS);

            if (cat == category) {
                categoryItem.glow();
            }

            inventory.setItem(cat.getSlot(), categoryItem.build(), event -> {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();

                if (cat != category) {
                    new KitRoomMenu(cat).open(player);
                    player.sendActionBar(ColorUtil.translateColorCodes("&#579af7Switched to " + cat.getDisplayName()));
                }
            });
        }
    }

    public void open(@NotNull Player player) {
        inventory.open(player);
    }
}
