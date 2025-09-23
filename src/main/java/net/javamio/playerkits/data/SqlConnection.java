package net.javamio.playerkits.data;

import lombok.Getter;
import net.javamio.playerkits.PlayerKits;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    public void createTables() { // no need to execute this asynchronously as it's only invoked on startup
        String[] statements = {
                """
            CREATE TABLE IF NOT EXISTS player_kits (
                player_uuid VARCHAR(36) NOT NULL,
                kit_number INT NOT NULL,
                contents TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (player_uuid, kit_number),
                INDEX idx_player_uuid (player_uuid),
                INDEX idx_updated_at (updated_at)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS kit_room (
                id INT AUTO_INCREMENT PRIMARY KEY,
                category VARCHAR(50) NOT NULL,
                item_data TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_category (category)
            )
            """
        };

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            for (String sql : statements) stmt.executeUpdate(sql);
            PlayerKits.LOGGER.info("Database tables created successfully");

        } catch (SQLException e) {
            PlayerKits.LOGGER.severe("Failed to create database tables: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
