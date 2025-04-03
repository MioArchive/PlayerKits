package net.javamio.playerkits;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public class PlayerKits extends JavaPlugin {

    @Getter
    public static PlayerKits instance;
    public static Logger LOGGER = Logger.getLogger("PlayerKits");

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

    }
}
