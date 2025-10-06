package net.javamio.playerkits.command.impl;

import net.javamio.playerkits.PlayerKits;
import net.javamio.playerkits.command.BukkitCommand;
import net.javamio.playerkits.data.kitroom.KitRoomCategory;
import net.javamio.playerkits.menu.KitRoomAdminMenu;
import net.javamio.playerkits.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerKitsCommand extends BukkitCommand {

    public PlayerKitsCommand() {
        super("playerkits", "playerkits.command.playerkits", "playerkit", "pk", "pkit");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            long start = System.currentTimeMillis();
            PlayerKits.getInstance().getKitRoom().reload();
            PlayerKits.getInstance().reloadConfig();
            long end = System.currentTimeMillis();
            player.sendMessage(ColorUtil.translateColorCodes("&aSuccessfully reloaded PlayerKits in " + (end - start) + "ms"));
            return;
        }

        if (args[0].equalsIgnoreCase("database")) {
            if (args[1].equalsIgnoreCase("reconnect")) {
                long start = System.currentTimeMillis();
                Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), (async) -> {
                    try {
                        Connection conn = PlayerKits.getInstance().getSqlConnection().getConnection();
                        if (conn != null && !conn.isClosed()) conn.close();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to close SQL connection", e);
                    }
                    PlayerKits.getInstance().getSqlConnection().setup();
                });
                long end = System.currentTimeMillis();
                player.sendMessage(ColorUtil.translateColorCodes("&aSuccessfully reconnected to the database in " + (end - start) + "ms"));
                return;
            }

            if (args[1].equalsIgnoreCase("disconnect")) {
                Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), (async) -> {
                    try {
                        Connection conn = PlayerKits.getInstance().getSqlConnection().getConnection();
                        if (conn != null && !conn.isClosed()) conn.close();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to close SQL connection", e);
                    }
                });
                player.sendMessage(ColorUtil.translateColorCodes("&aSuccessfully disconnected from the database"));
                return;
            }

            if (args[1].equalsIgnoreCase("connect")) {
                Bukkit.getScheduler().runTaskAsynchronously(PlayerKits.getInstance(),
                        () -> PlayerKits.getInstance().getSqlConnection().setup());
                player.sendMessage(ColorUtil.translateColorCodes("&aSuccessfully connected to the database"));
                return;
            }

            return;
        }

        if (args[0].equalsIgnoreCase("kitroom")) {
            if (args[1].equalsIgnoreCase("reload")) {
                long start = System.currentTimeMillis();
                PlayerKits.getInstance().getKitRoom().reload();
                long end = System.currentTimeMillis();
                player.sendMessage(ColorUtil.translateColorCodes("&aSuccessfully reloaded Kit Room in " + (end - start) + "ms"));
                return;
            }

            if (args[1].equalsIgnoreCase("edit")) {
                try {
                    KitRoomCategory category = KitRoomCategory.valueOf(args[2].toUpperCase());
                    new KitRoomAdminMenu(category).open(player);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ColorUtil.translateColorCodes("&cInvalid category: " + args[2]));
                }
            }

        }

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            return List.of("reload", "database", "kitroom");
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("kitroom")) {
                return List.of("reload", "edit");
            }

            if (args[0].equalsIgnoreCase("database")) {
                return List.of("reconnect", "disconnect", "connect");
            }
        }

        if (args.length == 3
                && args[0].equalsIgnoreCase("kitroom")
                && args[1].equalsIgnoreCase("edit")) {
            return Arrays.stream(KitRoomCategory.values())
                    .map(c -> c.name().toLowerCase())
                    .toList();
        }

        return suggestions;
    }

}
