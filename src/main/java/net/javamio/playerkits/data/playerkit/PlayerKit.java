package net.javamio.playerkits.data.playerkit;

import net.javamio.playerkits.PlayerKits;
import net.javamio.playerkits.data.playerkit.cache.PlayerDataCache;
import net.javamio.playerkits.util.SerializationUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerKit {

    private static final String SELECT_KIT_SQL = "SELECT contents FROM player_kits WHERE player_uuid = ? AND kit_number = ?";
    private static final String UPSERT_KIT_SQL = """
            INSERT INTO player_kits (player_uuid, kit_number, contents) 
            VALUES (?, ?, ?) 
            ON DUPLICATE KEY UPDATE contents = VALUES(contents), updated_at = CURRENT_TIMESTAMP
            """;
    private static final String DELETE_KIT_SQL = "DELETE FROM player_kits WHERE player_uuid = ? AND kit_number = ?";
    private static final String COUNT_KITS_SQL = "SELECT COUNT(*) FROM player_kits WHERE player_uuid = ?";

    public void loadAsync(UUID playerUUID, int index, Consumer<Map<Integer, ItemStack>> callback) {
        final UUID uuid = playerUUID;

        if (PlayerDataCache.hasKitCached(uuid, index)) {
            Bukkit.getGlobalRegionScheduler().execute(PlayerKits.getInstance(),
                    () -> callback.accept(PlayerDataCache.getCachedKit(uuid, index)));
            return;
        }

        Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), task -> {
            Map<Integer, ItemStack> contents = new HashMap<>();

            try (Connection connection = PlayerKits.getInstance().getSqlConnection().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(SELECT_KIT_SQL)) {
                stmt.setString(1, playerUUID.toString());
                stmt.setInt(2, index);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String data = rs.getString("contents");
                        contents = SerializationUtil.deserializeInventory(data);
                    }
                }
            } catch (SQLException e) {
                PlayerKits.LOGGER.severe("Failed to load kit " + index + " for player " + playerUUID + ": " + e.getMessage());
            }

            PlayerDataCache.cacheKit(uuid, index, contents);
            Map<Integer, ItemStack> finalContents = contents;

            Bukkit.getGlobalRegionScheduler().execute(PlayerKits.getInstance(), () -> callback.accept(finalContents));
        });
    }

    public void saveAsync(@NotNull UUID playerUUID, int index, @NotNull Map<Integer, ItemStack> contents) {
        final UUID uuid = playerUUID;

        PlayerDataCache.cacheKit(uuid, index, contents);

        Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), task -> {
            try (Connection connection = PlayerKits.getInstance().getSqlConnection().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(UPSERT_KIT_SQL)) {
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, index);
                stmt.setString(3, SerializationUtil.serializeInventory(contents));
                stmt.executeUpdate();
            } catch (SQLException e) {
                PlayerKits.LOGGER.severe("Failed to save kit " + index + " for player " + playerUUID + ": " + e.getMessage());
            }
        });
    }

    public void deleteAsync(@NotNull UUID playerUUID, int index) {
        final UUID uuid = playerUUID;

        PlayerDataCache.invalidateKit(uuid, index);

        Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), task -> {
            try (Connection connection = PlayerKits.getInstance().getSqlConnection().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(DELETE_KIT_SQL)) {
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, index);
                stmt.executeUpdate();
            } catch (SQLException e) {
                PlayerKits.LOGGER.severe("Failed to delete kit " + index + " for player " + playerUUID + ": " + e.getMessage());
            }
        });
    }

    public void hasKitAsync(@NotNull UUID playerUUID, int index, Consumer<Boolean> callback) {
        final UUID uuid = playerUUID;

        if (PlayerDataCache.hasKitCached(uuid, index)) {
            Bukkit.getGlobalRegionScheduler().execute(PlayerKits.getInstance(), () -> callback.accept(true));
            return;
        }

        Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), task -> {
            boolean exists = false;

            try (Connection connection = PlayerKits.getInstance().getSqlConnection().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(SELECT_KIT_SQL)) {
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, index);

                try (ResultSet rs = stmt.executeQuery()) {
                    exists = rs.next();
                }
            } catch (SQLException e) {
                PlayerKits.LOGGER.severe("Failed to check kit existence " + index + " for player " + playerUUID + ": " + e.getMessage());
            }

            boolean finalExists = exists;
            Bukkit.getGlobalRegionScheduler().execute(PlayerKits.getInstance(), () -> callback.accept(finalExists));
        });
    }

    public void getKitCountAsync(@NotNull UUID playerUUID, Consumer<Integer> callback) {
        final UUID uuid = playerUUID;

        Bukkit.getAsyncScheduler().runNow(PlayerKits.getInstance(), task -> {
            int count = 0;

            try (Connection connection = PlayerKits.getInstance().getSqlConnection().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(COUNT_KITS_SQL)) {
                stmt.setString(1, uuid.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                PlayerKits.LOGGER.severe("Failed to count kits for player " + playerUUID + ": " + e.getMessage());
            }

            int finalCount = count;
            Bukkit.getGlobalRegionScheduler().execute(PlayerKits.getInstance(), () -> callback.accept(finalCount));
        });
    }

}
