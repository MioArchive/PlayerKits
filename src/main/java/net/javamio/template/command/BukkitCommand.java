package net.javamio.template.command;

import net.javamio.template.PaperPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class BukkitCommand extends Command {

    private final String permission;

    public BukkitCommand(@NotNull String name, String permission, String... aliases) {
        super(name, name + " command", "/" + name,
                (aliases == null || aliases.length == 0 || aliases[0].isEmpty())
                        ? Collections.singletonList(name)
                        : Stream.concat(Stream.of(name), Arrays.stream(aliases)).toList()
        );
        this.permission = permission;
        registerCommand(this);
    }

    public abstract void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return super.tabComplete(sender, label, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (permission == null || sender.hasPermission(permission)) {
            onCommand(sender, label, args);
        } else {
            sender.sendRichMessage("<red>You don't have the required permission to execute this command.");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        return onTabComplete(sender, alias, args);
    }

    private void registerCommand(@NotNull Command command) {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            if (commandMap != null) {
                commandMap.register(command.getName(), command);
            } else {
                PaperPlugin.LOGGER.severe("CommandMap is null while registering command " + command.getName());
            }
        } catch (ReflectiveOperationException e) {
            PaperPlugin.LOGGER.severe("Failed to register command " + command.getName() + ": " + e.getMessage());
        }
    }
}
