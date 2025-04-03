package net.javamio.playerkits.storage.file;

import net.javamio.playerkits.PlayerKits;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class KitFile {

    private File file;
    private FileConfiguration config;

    public void init() {
        file = new File(PlayerKits.getInstance().getDataFolder(),"kit-data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                PlayerKits.LOGGER.warning("Failed to create kit-data-yml - " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public Optional<FileConfiguration> getConfig() {
        return Optional.ofNullable(config);
    }

    public void save() {
        if (config == null || file == null) {
            PlayerKits.LOGGER.warning("Cannot save config: Config or file is null!");
            return;
        }
        try {
            config.save(file);
        } catch (IOException e) {
            PlayerKits.LOGGER.warning("Failed to save kit-data.yml - " + e.getMessage());
        }
    }


    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
