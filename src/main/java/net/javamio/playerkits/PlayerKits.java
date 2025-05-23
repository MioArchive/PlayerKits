package net.javamio.playerkits;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPILogger;
import lombok.Getter;
import net.javamio.playerkits.command.KitCommand;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PlayerKits extends JavaPlugin {

    @Getter
    private static PlayerKits instance;

    @Override
    public void onLoad() {
        CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));
        CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this);
        config.setNamespace("playerkits");
        CommandAPI.onLoad(config);
    }

    @Override
    public void onEnable() {
        instance = this;
        CommandAPI.onEnable();

        new KitCommand();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
