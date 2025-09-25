package net.javamio.playerkits.command.impl;

import net.javamio.playerkits.PlayerKits;
import net.javamio.playerkits.command.BukkitCommand;
import net.javamio.playerkits.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KitLoadCommand extends BukkitCommand {

    private final int kitNumber;

    public KitLoadCommand(int kitNumber) {
        super("kit" + kitNumber, null, "k" + kitNumber);
        this.kitNumber = kitNumber;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length != 0) {
            player.sendMessage(ColorUtil.translateColorCodes("&cInvalid Usage: /" + label));
            return;
        }

        PlayerKits.getInstance().getPlayerKitManager().loadKit(player, kitNumber);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        return new ArrayList<>();
    }
}
