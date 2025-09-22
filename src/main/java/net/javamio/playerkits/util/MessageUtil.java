package net.javamio.playerkits.util;

import lombok.experimental.UtilityClass;
import net.javamio.playerkits.PlayerKits;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@UtilityClass
public class MessageUtil {

    public @NotNull String getPrefix() {
        return Objects.requireNonNull(PlayerKits.getInstance().getConfig().getString("messages.prefix"));
    }

    public String get(@NotNull String key) {
        return getPrefix() + PlayerKits.getInstance().getConfig().getString("messages." + key);
    }

    public String getWithOutPrefix(@NotNull String key) {
        return PlayerKits.getInstance().getConfig().getString("messages." + key);
    }

}
