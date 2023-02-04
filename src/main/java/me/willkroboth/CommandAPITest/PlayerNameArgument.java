package me.willkroboth.CommandAPITest;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class PlayerNameArgument extends CustomArgument<Player, String> {
    public PlayerNameArgument(String nodeName, String regex) {
        this(nodeName, Pattern.compile(regex));
    }

    private PlayerNameArgument(String nodeName, Pattern regex) {
        super(new StringArgument(nodeName), customArgumentInfo -> {
            String playerName = customArgumentInfo.currentInput();

            if(!regex.matcher(playerName).matches()) throw new CustomArgumentException("Player name is invalid!");

            return Bukkit.getPlayer(playerName);
        });
        replaceSuggestions(ArgumentSuggestions.strings(
                info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)
        ));
    }
}
