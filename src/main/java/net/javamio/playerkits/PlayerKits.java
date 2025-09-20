package net.javamio.playerkits;

import lombok.Getter;
import net.javamio.playerkits.data.SqlConnection;
import net.javamio.playerkits.data.kitroom.KitRoom;
import net.javamio.playerkits.listener.PlayerJoinListener;
import net.javamio.playerkits.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public class PlayerKits extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("PlayerKits");

    @Getter
    private static PlayerKits instance;
    private SqlConnection sqlConnection;
    private KitRoom kitRoom;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        sqlConnection = new SqlConnection();
        sqlConnection.setup();

        kitRoom = new KitRoom();
        kitRoom.setup();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), instance);

    }

    @Override
    public void onDisable() {

    }
}
