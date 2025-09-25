package net.javamio.playerkits.data.playerkit;

import lombok.RequiredArgsConstructor;
import net.javamio.playerkits.util.ColorUtil;
import net.javamio.playerkits.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerKitManager {

    private final PlayerKit kit;

    public void loadKit(Player player, int index) {
        kit.loadAsync(player.getUniqueId(), index, contents -> {
            if (contents.isEmpty()) {
                player.sendMessage(ColorUtil.translateColorCodes("&cKit " + index + " is empty!"));
                return;
            }

            player.getInventory().clear();

            for (Map.Entry<Integer, ItemStack> entry : contents.entrySet()) {
                int slot = entry.getKey();
                ItemStack item = entry.getValue();

                if (slot >= 0 && slot < player.getInventory().getSize()) {
                    player.getInventory().setItem(slot, item);
                }
            }

            player.sendMessage(ColorUtil.translateColorCodes("&aKit " + index + " has been loaded!"));
        });
    }

    public void saveKit(@NotNull Player player, int index) {
        Map<Integer, ItemStack> contents = new HashMap<>();
        ItemStack[] inventory = player.getInventory().getContents();

        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && !item.getType().isAir()) {
                contents.put(i, item.clone());
            }
        }

        if (contents.isEmpty()) {
            player.sendMessage(ColorUtil.translateColorCodes("&cKit " + index + " is empty!"));
            return;
        }

        kit.saveAsync(player.getUniqueId(), index, contents);
        player.sendMessage(ColorUtil.translateColorCodes("&aKit " + index + " has been saved!"));
    }

    public void deleteKit(@NotNull Player player, int index) {
        kit.deleteAsync(player.getUniqueId(), index);
        player.sendMessage(ColorUtil.translateColorCodes("&cKit " + index + " has been deleted!"));
    }
}
