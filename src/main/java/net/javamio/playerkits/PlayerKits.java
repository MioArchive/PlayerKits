package net.javamio.playerkits;

import lombok.Getter;
import net.javamio.playerkits.command.impl.KitRoomCommand;
import net.javamio.playerkits.command.impl.PlayerKitsCommand;
import net.javamio.playerkits.data.SqlConnection;
import net.javamio.playerkits.data.kitroom.KitRoom;
import net.javamio.playerkits.data.playerkit.cache.PlayerDataCache;
import net.javamio.playerkits.listener.PlayerJoinListener;
import net.javamio.playerkits.listener.PlayerQuitListener;
import net.javamio.playerkits.util.inventory.InventoryManager;
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
        sqlConnection.createTables();

        InventoryManager.register(instance);

        kitRoom = new KitRoom();
        kitRoom.setup();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), instance);

        new PlayerKitsCommand();
        new KitRoomCommand();

        //TODO(Mio) - Auto save
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> PlayerDataCache.unloadPlayer(player.getUniqueId()));
    }
}
