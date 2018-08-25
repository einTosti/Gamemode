package de.eintosti.gamemode.commands;

import de.eintosti.gamemode.Gamemode;
import de.eintosti.gamemode.inventories.ColourInventory;
import de.eintosti.gamemode.inventories.GamemodeInventory;
import de.eintosti.gamemode.inventories.InfoInventory;
import de.eintosti.gamemode.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author einTosti
 */
public class GamemodeCommand implements CommandExecutor {
    private static GamemodeCommand instance;

    public static GamemodeCommand getInstance() {
        if (instance == null) instance = new GamemodeCommand();
        return instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.getInstance().getString("not_a_player"));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            if (openGamemodeGui()) {
                GamemodeInventory.getInstance().openInventory(player);
            } else {
                showUsageMessage(player);
            }
        } else if (args.length == 1 || args.length == 2) {
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "info":
                        InfoInventory.getInstance().openInventory(player);
                        break;
                    case "rl":
                    case "reload":
                        reloadPlugin(player);
                        break;
                }
            }

            switch (args[0].toLowerCase()) {
                case "colour":
                case "color":
                    ColourInventory.getInstance().openInventory(player);
                    break;
                case "survival":
                case "s":
                case "0":
                    setGamemode(player, args, "gm.survival", GameMode.SURVIVAL, Utils.getInstance().getString("gamemode_survival"));
                    break;
                case "creative":
                case "c":
                case "1":
                    setGamemode(player, args, "gm.creative", GameMode.CREATIVE, Utils.getInstance().getString("gamemode_creative"));
                    break;
                case "adventure":
                case "a":
                case "2":
                    setGamemode(player, args, "gm.adventure", GameMode.ADVENTURE, Utils.getInstance().getString("gamemode_adventure"));
                    break;
                case "spectator":
                case "sp":
                case "3":
                    setGamemode(player, args, "gm.spectator", GameMode.SPECTATOR, Utils.getInstance().getString("gamemode_spectator"));
                    break;
                default:
                    showUsageMessage(player);
                    break;
            }
        } else {
            showUsageMessage(player);
        }
        return true;
    }

    /*
     * METHODS
     */
    private void showUsageMessage(Player player) {
        player.sendMessage(Utils.getInstance().getString("gm_usage"));
    }

    private void reloadPlugin(Player player) {
        if (!player.hasPermission("gm.reload")) {
            Utils.getInstance().showPermErrorMessage(player);
            return;
        }
        Gamemode.plugin.reloadConfig();
        player.sendMessage(Utils.getInstance().getString("gm_configReloaded").replace("%colour%", Utils.getInstance().mColour.toString()));
    }

    private boolean openGamemodeGui() {
        return Gamemode.plugin.getConfig().getBoolean("enableGui");
    }

    private void setGamemode(Player player, String args[], String permission, GameMode gameMode, String gameModeName) {
        switch (args.length) {
            case 1:
                setPlayerGamemode(player, permission, gameMode, gameModeName);
                break;
            case 2:
                setTargetGamemode(player, args, 1, gameMode, gameModeName);
                break;
            default:
                showUsageMessage(player);
                break;
        }
    }

    protected void setPlayerGamemode(Player player, String permission, GameMode gameMode, String gameModeName) {
        if (!player.hasPermission(permission)) {
            Utils.getInstance().showPermErrorMessage(player);
            return;
        }
        player.setGameMode(gameMode);
        player.sendMessage(Utils.getInstance().getString("gm_changed").replace("%gamemode%", Utils.getInstance().mColour + gameModeName));
    }

    protected void setTargetGamemode(Player player, String args[], int i, GameMode gameMode, String gameModeName) {
        if (!player.hasPermission("gm.setothers")) {
            Utils.getInstance().showPermErrorMessage(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[i]);
        if (target == null) {
            player.sendMessage(Utils.getInstance().getString("gm_playerNotFound"));
            return;
        }
        target.setGameMode(gameMode);
        target.sendMessage(Utils.getInstance().getString("gm_changed").replace("%gamemode%", Utils.getInstance().mColour + gameModeName));
    }
}
