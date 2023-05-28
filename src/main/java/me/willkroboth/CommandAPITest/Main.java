package me.willkroboth.CommandAPITest;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onLoad() {
        CommandAPI.onLoad(
                new CommandAPIBukkitConfig(this)
                        .verboseOutput(true)
        );
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        register();
    }

    public static void register() {
    }
}
