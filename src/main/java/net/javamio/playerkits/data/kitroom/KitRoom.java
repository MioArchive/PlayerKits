package net.javamio.playerkits.data.kitroom;

import lombok.Getter;
import net.javamio.playerkits.util.SerializationUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class KitRoom {

    @Getter
    private static FileConfiguration config;
    private static File file;

    public void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PlayerKits")).getDataFolder(),"kitroom.yml");

        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create kitroom.yml");
                }
            } catch (IOException e) {
                throw new IllegalStateException("Could not create kitroom.yml", e);
            }
        }
        reload();
    }

    public @NotNull ConfigurationSection section(@NotNull KitRoomCategory category) {
        if (!config.contains(category.getPath())) {
            config.createSection(category.getPath());
            save();
        }
        return Objects.requireNonNull(config.getConfigurationSection(category.getPath()));
    }

    public @NotNull List<ItemStack> itemStacks(@NotNull KitRoomCategory category) {
        reload();

        List<ItemStack> list = new ArrayList<>();
        ConfigurationSection section = section(category);

        for (String data : section.getStringList("contents")) {
            ItemStack stack = SerializationUtil.deserializeItem(data);
            if (stack != null) {
                list.add(stack);
            }
        }

        return list;
    }

    public boolean addItemStacks(@NotNull KitRoomCategory category, @NotNull List<@NotNull ItemStack> itemStacksToAdd) {
        reload();

        if (itemStacksToAdd.size() > 45) return false;
        if (itemStacks(category).size() + itemStacksToAdd.size() > 45) return false;

        List<String> serialized = new ArrayList<>();
        for (ItemStack itemStack : itemStacksToAdd) {
            String serialize = SerializationUtil.serializeItem(itemStack);
            if (serialize != null) serialized.add(serialize);
        }

        List<String> yaml = section(category).getStringList("contents");
        yaml.addAll(serialized);

        section(category).set("contents", yaml);
        save();
        reload();
        return true;
    }

    public void clear(@NotNull KitRoomCategory category) {
        reload();
        section(category).set("contents", null);
        save();
        reload();
    }

    public void clear() {
        for (KitRoomCategory category : KitRoomCategory.values()) {
            clear(category);
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save kitroom.yml", e);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

}
