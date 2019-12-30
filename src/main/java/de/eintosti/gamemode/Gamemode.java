package de.eintosti.gamemode;

import de.eintosti.gamemode.commands.*;
import de.eintosti.gamemode.listeners.InventoryClickListener;
import de.eintosti.gamemode.listeners.PlayerJoinListener;
import de.eintosti.gamemode.misc.Messages;
import de.eintosti.gamemode.misc.Utils;
import de.eintosti.gamemode.tabcomplete.GamemodeTabCompleter;
import org.bukkit.Bukkit;
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

        Utils.getInstance().thaw();
        getConfig().options().copyDefaults(true);
        saveConfig();
        Messages.getInstance().createMessageFile();

        Bukkit.getConsoleSender().sendMessage("Gamemode » Plugin §aenabled§r!");
    }

    @Override
    public void onDisable() {
        Utils.getInstance().freeze();
        saveConfig();
        plugin = null;
        Bukkit.getConsoleSender().sendMessage("Gamemode » Plugin §cdisabled§r!");
    }

    private void setInstance() {
        if (plugin == null) {
            plugin = this;
        } else {
            getLogger().warning("The plugin was loaded twice. Error!");
        }
    }

    private void registerListeners() {
        getPluginManager().registerEvents(new InventoryClickListener(), this);
        getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void registerTabCompleters() {
        this.getCommand("gm").setTabCompleter(new GamemodeTabCompleter());
    }

    private void registerExecutors() {
        getPluginCommand("gm").setExecutor(new GamemodeCommand());
        getPluginCommand("gma").setExecutor(new GamemodeAdventure());
        getPluginCommand("gmc").setExecutor(new GamemodeCreative());
        getPluginCommand("gms").setExecutor(new GamemodeSurvival());
        getPluginCommand("gmsp").setExecutor(new GamemodeSpectator());
    }

    private void manageFiles() {
        Utils.getInstance().gameMode = new File(getDataFolder() + File.separator);
        if (!Utils.getInstance().gameMode.exists()) {
            Utils.getInstance().gameMode.mkdirs();
        }
    }
}
