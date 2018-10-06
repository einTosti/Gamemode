package de.eintosti.gamemode.listeners;

import de.eintosti.gamemode.inventories.GamemodeInventory;
import de.eintosti.gamemode.misc.Utils;
import de.eintosti.gamemode.misc.fancymessage.mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author einTosti
 */
public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClickGamemode(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(Utils.getInstance().getString("gamemode_guiName"))) {
            event.setCancelled(true);

            ItemStack itemStack = event.getCurrentItem();
            Material itemType = itemStack.getType();

            if (itemType == Material.BOOK) {
                switch (event.getSlot()) {
                    case 10:
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    case 12:
                        player.setGameMode(GameMode.CREATIVE);
                        break;
                    case 14:
                        player.setGameMode(GameMode.ADVENTURE);
                        break;
                    case 16:
                        player.setGameMode(GameMode.SPECTATOR);
                        break;
                }
                GamemodeInventory.getInstance().openInventory(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClickColour(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(Utils.getInstance().getString("colour_guiName"))) {
            event.setCancelled(true);
            if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR){
             return;   
            }

            ItemStack itemStack = event.getCurrentItem();
            Material itemType = itemStack.getType();
            if (!player.hasPermission("gm.colour")) {
                return;
            }

            if (itemType == Material.WOOL) {
                short durability = itemStack.getDurability();
                switch (durability) {
                    case 0:
                        Utils.getInstance().setColour(player, ChatColor.WHITE, Utils.getInstance().getString("colour_white"));
                        break;
                    case 1:
                        Utils.getInstance().setColour(player, ChatColor.GOLD, Utils.getInstance().getString("colour_orange"));
                        break;
                    case 3:
                        Utils.getInstance().setColour(player, ChatColor.AQUA, Utils.getInstance().getString("colour_aqua"));
                        break;
                    case 4:
                        Utils.getInstance().setColour(player, ChatColor.YELLOW, Utils.getInstance().getString("colour_yellow"));
                        break;
                    case 5:
                        Utils.getInstance().setColour(player, ChatColor.GREEN, Utils.getInstance().getString("colour_lime"));
                        break;
                    case 6:
                        Utils.getInstance().setColour(player, ChatColor.LIGHT_PURPLE, Utils.getInstance().getString("colour_pink"));
                        break;
                    case 7:
                        Utils.getInstance().setColour(player, ChatColor.DARK_GRAY, Utils.getInstance().getString("colour_grey"));
                        break;
                    case 8:
                        Utils.getInstance().setColour(player, ChatColor.GRAY, Utils.getInstance().getString("colour_lightGrey"));
                        break;
                    case 9:
                        Utils.getInstance().setColour(player, ChatColor.DARK_AQUA, Utils.getInstance().getString("colour_cyan"));
                        break;
                    case 10:
                        Utils.getInstance().setColour(player, ChatColor.DARK_PURPLE, Utils.getInstance().getString("colour_purple"));
                        break;
                    case 11:
                        Utils.getInstance().setColour(player, ChatColor.BLUE, Utils.getInstance().getString("colour_blue"));
                        break;
                    case 13:
                        Utils.getInstance().setColour(player, ChatColor.DARK_GREEN, Utils.getInstance().getString("colour_green"));
                        break;
                    case 14:
                        Utils.getInstance().setColour(player, ChatColor.RED, Utils.getInstance().getString("colour_red"));
                        break;
                    case 15:
                        Utils.getInstance().setColour(player, ChatColor.BLACK, Utils.getInstance().getString("colour_black"));
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickInfo(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals("§8Plugin Information")) {
            event.setCancelled(true);

            ItemStack itemStack = event.getCurrentItem();
            if (itemStack.getType() == Material.SKULL_ITEM) {
                sendTwitterLink(player);
            }
        }
    }

    private void sendTwitterLink(Player player) {
        player.closeInventory();
        new FancyMessage("§7§m------------------------------------------\n§7 \n         §7Follow " + Utils.getInstance().mColour + "@einTosti§7 on " + Utils.getInstance().mColour + "Twitter §8» ").then(Utils.getInstance().mColour + "*click*\n§f").link("https://twitter.com/einTosti").then("\n§7§m------------------------------------------").send(player);
    }
}
