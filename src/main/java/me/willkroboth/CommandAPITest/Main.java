package me.willkroboth.CommandAPITest;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;

public class Main extends JavaPlugin {
    @Override
    public void onLoad() {
        CommandAPI.onLoad(
                new CommandAPIBukkitConfig(this)
                        .verboseOutput(true)
                        .dispatcherFile(new File(getDataFolder(), "dispatcher.json"))
        );
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        new CommandAPICommand("register")
                .withArguments(new StringArgument("command"))
                .executes(info -> {
                    String command = info.args().getUnchecked("command");
                    assert command != null;

                    new CommandAPICommand(command)
                            .executes(subInfo -> {
                                subInfo.sender().sendMessage("You ran the " + command + " command!");
                            })
                            .register();

                    updateCommandMap();
                })
                .register();

        new CommandAPICommand("unregister")
                .withArguments(new StringArgument("command"))
                .executes(info -> {
                    String command = info.args().getUnchecked("command");
                    assert command != null;

                    CommandAPI.unregister(command);

                    updateCommandMap();
                })
                .register();
    }

    private static void updateCommandMap() throws WrapperCommandSyntaxException {
        Server server = Bukkit.getServer();
        try {
            Method setVanillaCommands = server.getClass().getDeclaredMethod("setVanillaCommands", boolean.class);
            setVanillaCommands.setAccessible(true);
            setVanillaCommands.invoke(server, false);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw CommandAPI.failWithString("Could not update commands: " + e.getMessage());
        }
    }
}
