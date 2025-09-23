package net.javamio.playerkits.menu;

import net.javamio.playerkits.PlayerKits;
import net.javamio.playerkits.data.kitroom.KitRoomCategory;
import net.javamio.playerkits.util.ColorUtil;
import net.javamio.playerkits.util.inventory.InventoryBuilder;
import net.javamio.playerkits.util.inventory.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class KitRoomAdminMenu implements Listener {

    private final InventoryBuilder inventory;
    private final KitRoomCategory category;

    public KitRoomAdminMenu(KitRoomCategory category) {
        this.category = category;
        this.inventory = new InventoryBuilder(54, ColorUtil.translateColorCodes("&#579af7Modifying Kit Room"), false);

        Bukkit.getPluginManager().registerEvents(this, PlayerKits.getInstance());
        init();
    }

    private void init() {
        List<Integer> border = List.of(45, 46, 47, 51, 52, 53);
        List<ItemStack> kitContents = PlayerKits.getInstance().getKitRoom().itemStacks(category);

        border.forEach(slot -> inventory.setItem(slot, ItemBuilder.item(Material.BLACK_STAINED_GLASS_PANE)
                .name(ColorUtil.translateColorCodes(" "))
                .build(), event -> event.setCancelled(true)));

        for (int i = 0; i < Math.min(kitContents.size(), 45); i++) {
            ItemStack item = kitContents.get(i);
            if (item != null && !item.getType().isAir()) {
                inventory.setItem(i, item.clone(), event -> event.setCancelled(false));
            }
        }

        List<Component> lore = new ArrayList<>();

        lore.add(ColorUtil.translateColorCodes(" &fThis is the Kit Room for the "));
        lore.add(ColorUtil.translateColorCodes(" &f" + category.getDisplayName() + " &fcategory."));
        lore.add(ColorUtil.translateColorCodes(" &fItems in this category will be "));
        lore.add(ColorUtil.translateColorCodes(" &fshown in the Kit Room."));

        inventory.setItem(49, ItemBuilder.item(Material.SPECTRAL_ARROW)
                .name(ColorUtil.translateColorCodes("&#579af7Help"))
                .lore(lore)
                .build(), event -> event.setCancelled(true));

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(inventory.getInventory())) return;

        final Inventory eventInventory = event.getInventory();
        final Player player = (Player) event.getPlayer();

        final List<ItemStack> toAdd = getItemsFromInventory(eventInventory);

        boolean succeeded = PlayerKits.getInstance().getKitRoom().addItemStacks(this.category, toAdd);

        if (succeeded) {
            player.sendActionBar(ColorUtil.translateColorCodes("&#00ff00Added " + toAdd.size() + " items to the Kit Room!"));
        } else {
            player.sendActionBar(ColorUtil.translateColorCodes("&#ff0000An error has occurred. Failed to add items."));
        }

        Bukkit.getAsyncScheduler().runDelayed(PlayerKits.getInstance(), task ->
                HandlerList.unregisterAll(this), 10 * 50, TimeUnit.MILLISECONDS);
    }

    @NotNull
    private static List<ItemStack> getItemsFromInventory(Inventory eventInventory) {
        List<Integer> possibilities = List.of(
                0,  1,  2,  3,  4,  5,  6,  7,  8,
                9,  10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        );

        List<ItemStack> toAdd = new ArrayList<>();

        possibilities.forEach(slot -> {
            if (eventInventory.getItem(slot) != null) {
                if (Objects.requireNonNull(eventInventory.getItem(slot)).getType() != Material.AIR) {
                    toAdd.add(eventInventory.getItem(slot));
                }
            }
        });

        return toAdd;
    }

    public void open(@NotNull Player player) {
        inventory.open(player);
    }
}
