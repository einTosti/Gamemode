package de.eintosti.gamemode.tabcomplete;

import de.eintosti.gamemode.misc.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author einTosti
 */
public class GamemodeTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command label, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.getInstance().getString("not_a_player", null));
            return null;
        }

        if ((label.getName().equalsIgnoreCase("gamemode") || label.getName().equalsIgnoreCase("gm")) && args.length == 1) {
            ArrayList<String> argumentsList = new ArrayList<>();

            for (Arguments arguments : Arguments.values()) {
                if (arguments.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                    argumentsList.add(arguments.name());
                }
            }
            return argumentsList;
        }
        return null;
    }

    private enum Arguments {
        creative,
        survival,
        adventure,
        spectator,
        colour,
        info,
        reload
    }
}
