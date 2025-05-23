package net.javamio.playerkits.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.javamio.playerkits.cache.KitCache;
import net.javamio.playerkits.object.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitCommand {

    public KitCommand() {
        List<String> actions = List.of("save", "load", "view");

        new CommandAPICommand("kit")
                .withPermission("playerkits.kit")
                .withArguments(new StringArgument("action")
                        .replaceSuggestions(ArgumentSuggestions.strings("save", "load", "view"))
                )
                .withArguments(new IntegerArgument("id", 1, 16))
                .executesPlayer(this::onCommand)
                .register();
    }

    private void onCommand(Player player, CommandArguments arguments) {
        String action = (String) arguments.get("action");
        Integer kitId = (Integer) arguments.get("id");

        if (action == null || kitId == null) {
            player.sendMessage("§cInvalid usage! Use 'save', 'load' or 'view'.");
            return;
        }

        switch (action.toLowerCase()) {
            case "save" -> saveKit(player, kitId);
            case "load" -> loadKit(player, kitId);
            case "view" -> viewKit(player, kitId);
            default -> player.sendMessage("§cInvalid usage! Use 'save', 'load' or 'view'.");
        }
    }

    private void saveKit(Player player, int kitId) {
        UUID playerUUID = player.getUniqueId();

        Map<Integer, ItemStack> contents = new HashMap<>();
        ItemStack[] inventoryContents = player.getInventory().getContents();
        for (int i = 0; i < inventoryContents.length; i++) {
            if (inventoryContents[i] != null) {
                contents.put(i, inventoryContents[i]);
            }
        }

        Kit kit = new Kit(playerUUID, kitId, contents);
        KitCache.addKit(playerUUID, kit);

        player.sendMessage("Successfully saved kit (Slot: " + kitId + ")!");
    }

    private void viewKit(Player player, int kitId) {
        UUID playerUUID = player.getUniqueId();

        Map<Integer, Kit> playerKits = KitCache.getKits(playerUUID);
        Kit kit = playerKits.get(kitId);

        if (kit == null) {
            player.sendMessage("§cKit " + kitId + " does not exist.");
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 45, "Kit -> " + kitId);

        kit.getContent().forEach((slot, item) -> {
            if (item != null) {
                inventory.setItem(slot, item);
            }
        });

        player.openInventory(inventory);
    }

    private void loadKit(Player player, int kitId) {
        UUID playerUUID = player.getUniqueId();

        Map<Integer, Kit> playerKits = KitCache.getKits(playerUUID);
        Kit kit = playerKits.get(kitId);

        if (kit == null || kit.getContent().isEmpty()) {
            player.sendMessage("§cKit " + kitId + " is empty or does not exist!");
            return;
        }

        player.getInventory().clear();
        kit.getContent().forEach((slot, item) -> {
            if (item != null) {
                player.getInventory().setItem(slot, item);
            }
        });

        player.sendMessage("Successfully loaded kit (Slot: " + kitId + ")!");
    }
}
