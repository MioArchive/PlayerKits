package net.javamio.playerkits.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.javamio.playerkits.PlayerKits;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager {

    @Getter
    private HikariDataSource dataSource;

    private String hostname, port, database, username, password;
    private int minimumIdle, maximumPoolSize;
    private long connectionTimeout;
    @Getter
    private boolean disabled;

    public ConnectionPoolManager() {
        init();
        setupPool();
    }

    private void init() {
        FileConfiguration config = PlayerKits.getInstance().getConfig();
        hostname = config.getString("sql-configuration.hostname");
        username = config.getString("sql-configuration.username");
        password = config.getString("sql-configuration.password");
        database = config.getString("sql-configuration.database");
        port = config.getString("sql-configuration.port");

        minimumIdle = config.getInt("sql-configuration.hikariCP-configuration.minimum-idle");
        maximumPoolSize = config.getInt("sql-configuration.hikariCP-configuration.maximum-pool-size");
        connectionTimeout = config.getLong("sql-configuration.hikariCP-configuration.connection-timeout");
    }

    private void setupPool() {
        try {
            PlayerKits.getInstance().debug("Starting connection pool with the following configuration:");
            PlayerKits.getInstance().debug("Hostname: " + hostname);
            PlayerKits.getInstance().debug("Username: " + username);
            PlayerKits.getInstance().debug("Password: " + "*".repeat(password.length()));
            PlayerKits.getInstance().debug("Database: " + database);
            PlayerKits.getInstance().debug("Port: " + port);
            PlayerKits.getInstance().debug("Minimum Idle: " + minimumIdle);
            PlayerKits.getInstance().debug("Maximum Pool Size: " + maximumPoolSize);
            PlayerKits.getInstance().debug("Connection Timeout: " + connectionTimeout);
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.setUsername(username);
            config.setPassword(password);
            config.setMinimumIdle(minimumIdle);
            config.setMaximumPoolSize(maximumPoolSize);
            config.setConnectionTimeout(connectionTimeout);
            dataSource = new HikariDataSource(config);
            disabled = false;
            PlayerKits.LOGGER.info("Connection pool started");
        } catch (Exception e) {
            PlayerKits.LOGGER.warning("There was an error establishing a connection to your SQL provider. Be sure all of your connection information is correct.");
            disabled = true;
        }
    }

    public Connection getConnection() {
        try {
            if (dataSource.isClosed()) {
                PlayerKits.LOGGER.info("Connection pool is closed");
                return null;
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting connection");
        }
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public void close(Connection connection) {
        if (connection != null) try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}