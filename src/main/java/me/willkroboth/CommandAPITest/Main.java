package me.willkroboth.CommandAPITest;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
        new LocationArgument("");

        new CommandAPICommand("warn")
                .withPermission("my.super.cool.perm")
                .withArguments(
                        new StringArgument("player").replaceSuggestions(ArgumentSuggestions.strings(
                                info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)
                        )),
                        new GreedyStringArgument("reason")
                )
                .executes((sender, args) -> {
                    String playerName = (String) args.get(0);

                    // Check player name
                    if(!playerName.matches("coolRegexExpression")) throw CommandAPI.failWithString("Player name is invalid");

                    // Get player
                    Player player = Bukkit.getPlayer(playerName);
                }, ExecutorType.CONSOLE, ExecutorType.PLAYER)
                .register();
    }
}
