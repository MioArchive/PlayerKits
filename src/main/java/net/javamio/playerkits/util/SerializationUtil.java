package net.javamio.playerkits.util;

import lombok.experimental.UtilityClass;
import net.javamio.playerkits.PlayerKits;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@UtilityClass
public class SerializationUtil {

    public @Nullable String serializeItem(@NotNull ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            PlayerKits.LOGGER.severe("Failed to serialize item: " + e.getMessage());
            return null;
        }
    }

    public @Nullable ItemStack deserializeItem(@Nullable String base64) {
        if (base64 == null) return null;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (ItemStack) dataInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            PlayerKits.LOGGER.severe("Failed to deserialize item: " + e.getMessage());
            return null;
        }
    }
}
