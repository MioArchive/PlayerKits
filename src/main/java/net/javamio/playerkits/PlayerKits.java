package net.javamio.playerkits;

import lombok.Getter;
import net.javamio.playerkits.storage.mysql.ConnectionPoolManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public class PlayerKits extends JavaPlugin {

    @Getter
    public static PlayerKits instance;
    public static final Logger LOGGER = Logger.getLogger("PlayerKits");
    private ConnectionPoolManager connectionPoolManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        connectionPoolManager = new ConnectionPoolManager();
    }

    public void debug(String message) {
        if (getConfig().getBoolean("debug-mode")) {
            LOGGER.info(message);
        }
    }
}
