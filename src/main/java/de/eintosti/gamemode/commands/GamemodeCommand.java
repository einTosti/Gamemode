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

import java.util.UUID;

/**
 * @author einTosti
 */
public class GamemodeCommand implements CommandExecutor {
    private static GamemodeCommand instance;

    public static GamemodeCommand getInstance() {
        if (instance == null) instance = new GamemodeCommand();
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.getInstance().getString("not_a_player", null));
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        switch (args.length) {
            case 0:
                if (openGamemodeGui()) {
                    GamemodeInventory.getInstance().openInventory(player);
                } else {
                    showUsageMessage(player);
                }
                break;
            case 1:
            case 2:
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "info":
                            InfoInventory.getInstance().openInventory(player);
                            return true;
                        case "rl":
                        case "reload":
                            reloadPlugin(player);
                            return true;
                    }
                }
                switch (args[0].toLowerCase()) {
                    case "colour":
                    case "color":
                        ColourInventory.getInstance().openInventory(player);
                        return true;
                    case "survival":
                    case "s":
                    case "0":
                        setGamemode(player, args, "gm.survival", GameMode.SURVIVAL, Utils.getInstance().getString("gamemode_survival", uuid));
                        return true;
                    case "creative":
                    case "c":
                    case "1":
                        setGamemode(player, args, "gm.creative", GameMode.CREATIVE, Utils.getInstance().getString("gamemode_creative", uuid));
                        return true;
                    case "adventure":
                    case "a":
                    case "2":
                        setGamemode(player, args, "gm.adventure", GameMode.ADVENTURE, Utils.getInstance().getString("gamemode_adventure", uuid));
                        return true;
                    case "spectator":
                    case "sp":
                    case "3":
                        setGamemode(player, args, "gm.spectator", GameMode.SPECTATOR, Utils.getInstance().getString("gamemode_spectator", uuid));
                        return true;
                    default:
                        showUsageMessage(player);
                        return true;
                }
            default:
                showUsageMessage(player);
                return true;
        }
        return true;
    }

    private void showUsageMessage(Player player) {
        player.sendMessage(Utils.getInstance().getString("gm_usage", player.getUniqueId()));
    }

    private void reloadPlugin(Player player) {
        UUID uuid = player.getUniqueId();
        if (!player.hasPermission("gm.reload")) {
            Utils.getInstance().showPermErrorMessage(player);
            return;
        }
        Gamemode.plugin.reloadConfig();
        player.sendMessage(Utils.getInstance().getString("gm_configReloaded", uuid).replace("%colour%", Utils.getInstance().getColour(uuid).getAsString()));
    }

    private boolean openGamemodeGui() {
        return Gamemode.plugin.getConfig().getBoolean("enableGui");
    }

    private void setGamemode(Player player, String[] args, String permission, GameMode gameMode, String gameModeName) {
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
        UUID uuid = player.getUniqueId();
        if (!player.hasPermission(permission)) {
            Utils.getInstance().showPermErrorMessage(player);
            return;
        }
        player.setGameMode(gameMode);
        player.sendMessage(Utils.getInstance().getString("gm_changed", uuid).replace("%gamemode%", Utils.getInstance().getColour(uuid).getAsString() + gameModeName));
    }

    protected void setTargetGamemode(Player player, String[] args, int i, GameMode gameMode, String gameModeName) {
        if (!player.hasPermission("gm.setothers")) {
            Utils.getInstance().showPermErrorMessage(player);
            return;
        }
        Player target = Bukkit.getPlayer(args[i]);
        if (target == null) {
            player.sendMessage(Utils.getInstance().getString("gm_playerNotFound", player.getUniqueId()));
            return;
        }
        target.setGameMode(gameMode);
        target.sendMessage(Utils.getInstance().getString("gm_changed", target.getUniqueId()).replace("%gamemode%", Utils.getInstance().getColour(target.getUniqueId()).getAsString() + gameModeName));
    }
}
