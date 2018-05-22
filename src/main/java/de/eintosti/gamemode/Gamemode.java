package de.eintosti.gamemode;

import de.eintosti.gamemode.commands.*;
import de.eintosti.gamemode.listeners.InventoryClick;
import de.eintosti.gamemode.listeners.PlayerJoin;
import de.eintosti.gamemode.misc.Messages;
import de.eintosti.gamemode.misc.Utils;
import de.eintosti.gamemode.tabcomplete.GamemodeTC;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static org.bukkit.Bukkit.getPluginCommand;
import static org.bukkit.Bukkit.getPluginManager;

/**
 * @author einTosti
 */
public class Gamemode extends JavaPlugin {
    public static Gamemode plugin = null;

    @Override
    public void onEnable() {
        setInstance();

        registerExecutors();
        registerListeners();
        registerTabCompleters();

        manageFiles();
        getConfig().options().copyDefaults(true);
        saveConfig();
        Messages.getInstance().createMessageFile();

        getLogger().info("Plugin activated");
    }

    @Override
    public void onDisable() {
        plugin = null;
        saveConfig();
        getLogger().info("Plugin deactivated");
    }

    private void setInstance() {
        if (plugin == null) {
            plugin = this;
        } else {
            getLogger().warning("The plugin was loaded twice. Error!");
        }
    }

    private void registerListeners() {
        getPluginManager().registerEvents(new InventoryClick(), this);
        getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    private void registerTabCompleters() {
        this.getCommand("gm").setTabCompleter(new GamemodeTC());
    }

    private void registerExecutors() {
        getPluginCommand("gm").setExecutor(new GamemodeCommand());
        getPluginCommand("gma").setExecutor(new GamemodeAdventure());
        getPluginCommand("gmc").setExecutor(new GamemodeCreative());
        getPluginCommand("gms").setExecutor(new GamemodeSurvival());
        getPluginCommand("gmsp").setExecutor(new GamemodeSpectator());
    }

    private void manageFiles() {
        Utils.getInstance().mGameMode = new File(getDataFolder() + File.separator);
        if (!Utils.getInstance().mGameMode.exists()) {
            Utils.getInstance().mGameMode.mkdirs();
        }

        Utils.getInstance().mColourFile = new File(Utils.getInstance().mGameMode, Utils.getInstance().FILENAME);
        if (!Utils.getInstance().mColourFile.exists()) {
            Utils.getInstance().saveColour();
        } else {
            Utils.getInstance().readColour();
        }
    }
}
