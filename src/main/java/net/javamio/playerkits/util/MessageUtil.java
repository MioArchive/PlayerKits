package net.javamio.playerkits.util;

import lombok.experimental.UtilityClass;
import net.javamio.playerkits.PlayerKits;

@UtilityClass
public class MessageUtil {

    public String getPrefix() {
        return PlayerKits.getInstance().getConfig().getString("messages.prefix");
    }

    public String get(String key) {
        return getPrefix() + PlayerKits.getInstance().getConfig().getString("messages." + key);
    }

    public String getWithOutPrefix(String key) {
        return PlayerKits.getInstance().getConfig().getString("messages." + key);
    }

}
