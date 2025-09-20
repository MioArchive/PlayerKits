package net.javamio.template;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public class PaperPlugin extends JavaPlugin {

    public static Logger LOGGER = Logger.getLogger("PaperPlugin-Template");

    @Getter
    private static PaperPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        LOGGER.info("PaperPlugin-Template has been successfully enabled");
    }

}
