package de.eintosti.gamemode.misc;

import de.eintosti.gamemode.Gamemode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author einTosti
 */
public class Messages {
    public HashMap<String, String> mMessageData = new HashMap<>();
    private static Messages instance;

    public static synchronized Messages getInstance() {
        if (instance == null) instance = new Messages();
        return instance;
    }

    public void createMessageFile() {
        File file = new File(Gamemode.plugin.getDataFolder() + File.separator + "messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        StringBuilder sb = new StringBuilder();
        addLine(sb, "###########################################");
        addLine(sb, "#      Gamemode Plugin » by einTosti      #");
        addLine(sb, "#                                         #");
        addLine(sb, "#               Message File              #");
        addLine(sb, "###########################################");
        addLine(sb, "");
        addLine(sb, "# %prefix% is replaced with the prefix");
        addLine(sb, "# %colour% is replaced with the current colour");
        addLine(sb, "");
        addLine(sb, "# ---------");
        addLine(sb, "# MESSAGES");
        addLine(sb, "# ---------");
        setMessage(sb, config, "prefix", "&7● %colour%GM &8»");
        setMessage(sb, config, "no_permission", "%prefix% &7You do not have enough &cPermission &7for that!");
        setMessage(sb, config, "not_a_player", "%prefix% You have to be a player to use this command!");
        addLine(sb, "");
        setMessage(sb, config, "colour_changed", "%prefix% &7Colour changed to %colour%&7.");
        addLine(sb, "");
        setMessage(sb, config, "gm_usage", "%prefix% &7Usage&8: &c/gm <0,1,2,3> [Player]");
        setMessage(sb, config, "gm_changed", "%prefix% &7Your gamemode was set to %gamemode%&7.");
        setMessage(sb, config, "gm_playerNotFound", "%prefix% &7Error&8: &cPlayer not found!");
        setMessage(sb, config, "gm_configReloaded", "%prefix% &7The %colour%config &7was %colour%reloaded&7.");
        addLine(sb, "");
        setMessage(sb, config, "gma_usage", "%prefix% &7Usage&8: &c/gma [Player]");
        setMessage(sb, config, "gmc_usage", "%prefix% &7Usage&8: &c/gmc [Player]");
        setMessage(sb, config, "gms_usage", "%prefix% &7Usage&8: &c/gms [Player]");
        setMessage(sb, config, "gmsp_usage", "%prefix% &7Usage&8: &c/gmsp [Player]");
        addLine(sb, "");
        addLine(sb, "# ---------");
        addLine(sb, "# GAMEMODES");
        addLine(sb, "# ---------");
        setMessage(sb, config, "gamemode_survival", "Survival");
        setMessage(sb, config, "gamemode_creative", "Creative");
        setMessage(sb, config, "gamemode_adventure", "Adventure");
        setMessage(sb, config, "gamemode_spectator", "Spectator");
        addLine(sb, "");
        addLine(sb, "# ---------");
        addLine(sb, "# GUIs");
        addLine(sb, "# ---------");
        setMessage(sb, config, "colour_guiName", "&8Choose a colour");
        addLine(sb, "");
        setMessage(sb, config, "colour_red", "&cRed");
        setMessage(sb, config, "colour_orange", "&6Orange");
        setMessage(sb, config, "colour_yellow", "&eYellow");
        setMessage(sb, config, "colour_pink", "&dPink");
        setMessage(sb, config, "colour_purple", "&5Purple");
        setMessage(sb, config, "colour_lime", "&aLime");
        setMessage(sb, config, "colour_green", "&2Green");
        setMessage(sb, config, "colour_blue", "&9Blue");
        setMessage(sb, config, "colour_cyan", "&3Cyan");
        setMessage(sb, config, "colour_aqua", "&bAqua");
        setMessage(sb, config, "colour_white", "&fWhite");
        setMessage(sb, config, "colour_lightGrey", "&7Light Grey");
        setMessage(sb, config, "colour_grey", "&8Grey");
        setMessage(sb, config, "colour_black", "&0Black");
        addLine(sb, "");
        setMessage(sb, config, "gamemode_guiName", "&8Choose a gamemode");
        setMessage(sb, config, "gamemode_lore", "&7&oChanges your gamemode to %gamemode%.");

        try {
            FileWriter fw = new FileWriter(file, false);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String message : config.getConfigurationSection("").getKeys(false)) {
            mMessageData.put(message, config.getString(message));
        }
    }

    private void addLine(StringBuilder sb, String value) {
        sb.append(value).append("\n");
    }

    private void setMessage(StringBuilder sb, FileConfiguration config, String key, String defaultValue) {
        String value = config.getString(key, defaultValue);
        sb.append(key).append(": '").append(value).append("'").append("\n");
    }
}
