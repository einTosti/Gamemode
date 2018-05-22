package de.eintosti.gamemode.listeners;

import de.eintosti.gamemode.Gamemode;
import de.eintosti.gamemode.misc.fancymessage.mkremins.fanciful.FancyMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author einTosti
 */
public class PlayerJoin implements Listener {
    private String mKey = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=";
    private String mPluginVersion;
    private PluginDescriptionFile pdf = Gamemode.plugin.getDescription();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("gm.notify") && Gamemode.plugin.getConfig().getBoolean("versionChecker")) {
            this.versionChecker();
            if (!pdf.getVersion().equals(mPluginVersion)) {
                new FancyMessage("§7§m-----------------------------------------\n§7 \n             §7Hey, there is a §bnew version\n                §7of §bGamemode §7available!\n§7 \n             §7You can download it ").then("§3*here*\n").link("https://www.spigotmc.org/resources/.44682/").then("§7 \n§7§m-----------------------------------------").send(player);
            }
        }
    }

    private void versionChecker() {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL("https://www.spigotmc.org/api/general.php")).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write((mKey + '꺊').getBytes("UTF-8"));
            mPluginVersion = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
        } catch (IOException e) {
            Gamemode.plugin.getLogger().warning("Could not make connection to SpigotMC.org");
            e.printStackTrace();
        }
    }
}
