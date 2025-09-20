package net.javamio.playerkits.data;

import lombok.Getter;
import net.javamio.playerkits.PlayerKits;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter
public class SqlConnection {

    @Getter
    private Connection connection = null;

    public void setup() {
        FileConfiguration config = PlayerKits.getInstance().getConfig();
        String host = config.getString("sql-configuration.host");
        String port = config.getString("sql-configuration.port");
        String database = config.getString("sql-configuration.database");
        String username = config.getString("sql-configuration.username");
        String password = config.getString("sql-configuration.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        try {
            Properties properties = new Properties();
            properties.put("user", username);
            properties.put("password", password);
            properties.put("autoReconnect", "true");
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
