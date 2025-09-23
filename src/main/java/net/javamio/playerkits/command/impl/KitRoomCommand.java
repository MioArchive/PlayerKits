package net.javamio.playerkits.command.impl;

import net.javamio.playerkits.command.BukkitCommand;
import net.javamio.playerkits.menu.KitRoomMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitRoomCommand extends BukkitCommand {

    public KitRoomCommand() {
        super("kitroom",null);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        new KitRoomMenu().open(player);
    }
}
