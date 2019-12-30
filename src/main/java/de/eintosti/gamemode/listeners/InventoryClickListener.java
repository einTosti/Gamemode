package de.eintosti.gamemode.listeners;

import de.eintosti.gamemode.inventories.GamemodeInventory;
import de.eintosti.gamemode.misc.Colour;
import de.eintosti.gamemode.misc.Utils;
import de.eintosti.gamemode.misc.external.XMaterial;
import de.eintosti.gamemode.misc.external.fancymessage.mkremins.fanciful.FancyMessage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author einTosti
 */
public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClickGamemode(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(Utils.getInstance().getString("gamemode_guiName", player.getUniqueId())))
            return;
        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;
        Material itemType = itemStack.getType();

        if (itemType == XMaterial.BOOK.parseMaterial()) {
            switch (event.getSlot()) {
                case 10:
                    if (player.hasPermission("gm.survival")) {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                    break;
                case 12:
                    if (player.hasPermission("gm.creative")) {
                        player.setGameMode(GameMode.CREATIVE);
                    }
                    break;
                case 14:
                    if (player.hasPermission("gm.adventure")) {
                        player.setGameMode(GameMode.ADVENTURE);
                    }
                    break;
                case 16:
                    if (player.hasPermission("gm.spectator")) {
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                    break;
            }
            GamemodeInventory.getInstance().openInventory(player);
        }
    }

    @EventHandler
    public void onInventoryClickColour(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(Utils.getInstance().getString("colour_guiName", player.getUniqueId())))
            return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        ItemStack itemStack = event.getCurrentItem();
        if (!player.hasPermission("gm.colour")) {
            player.closeInventory();
            return;
        }
        if (itemStack.getType().toString().contains("WOOL")) {
            switch (event.getSlot()) {
                case 10:
                    Utils.getInstance().setColour(player, Colour.RED, Utils.getInstance().getString("colour_red", player.getUniqueId()));
                    break;
                case 11:
                    Utils.getInstance().setColour(player, Colour.ORANGE, Utils.getInstance().getString("colour_orange", player.getUniqueId()));
                    break;
                case 12:
                    Utils.getInstance().setColour(player, Colour.YELLOW, Utils.getInstance().getString("colour_yellow", player.getUniqueId()));
                    break;
                case 13:
                    Utils.getInstance().setColour(player, Colour.PINK, Utils.getInstance().getString("colour_pink", player.getUniqueId()));
                    break;
                case 14:
                    Utils.getInstance().setColour(player, Colour.PURPLE, Utils.getInstance().getString("colour_purple", player.getUniqueId()));
                    break;
                case 15:
                    Utils.getInstance().setColour(player, Colour.LIME, Utils.getInstance().getString("colour_lime", player.getUniqueId()));
                    break;
                case 16:
                    Utils.getInstance().setColour(player, Colour.GREEN, Utils.getInstance().getString("colour_green", player.getUniqueId()));
                    break;
                case 19:
                    Utils.getInstance().setColour(player, Colour.BLUE, Utils.getInstance().getString("colour_blue", player.getUniqueId()));
                    break;
                case 20:
                    Utils.getInstance().setColour(player, Colour.CYAN, Utils.getInstance().getString("colour_cyan", player.getUniqueId()));
                    break;
                case 21:
                    Utils.getInstance().setColour(player, Colour.LIGHT_BLUE, Utils.getInstance().getString("colour_aqua", player.getUniqueId()));
                    break;
                case 22:
                    Utils.getInstance().setColour(player, Colour.WHITE, Utils.getInstance().getString("colour_white", player.getUniqueId()));
                    break;
                case 23:
                    Utils.getInstance().setColour(player, Colour.LIGHT_GREY, Utils.getInstance().getString("colour_lightGrey", player.getUniqueId()));
                    break;
                case 24:
                    Utils.getInstance().setColour(player, Colour.GREY, Utils.getInstance().getString("colour_grey", player.getUniqueId()));
                    break;
                case 25:
                    Utils.getInstance().setColour(player, Colour.BLACK, Utils.getInstance().getString("colour_black", player.getUniqueId()));
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClickInfo(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§8Plugin Information")) return;
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;
        if (itemStack.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
            player.closeInventory();
            new FancyMessage("§7§m                                                               \n§7\n §7Check out " + Utils.getInstance().getColour(uuid).getAsString() + "einTosti's §7other plugins on ").then(Utils.getInstance().getColour(uuid).getAsString() + "§l§oSpigotMC").link("https://www.spigotmc.org/resources/authors/eintosti.357528/").then("\n§7\n§7§m                                                               ").send(player);
        }
    }
}
