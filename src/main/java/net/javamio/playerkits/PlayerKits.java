package net.javamio.playerkits;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PlayerKits extends JavaPlugin {

    @Getter
    private static PlayerKits instance;

    @Override
    public void onEnable() {
        instance = this;
    }

}
