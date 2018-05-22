package de.eintosti.gamemode.inventories;

import de.eintosti.gamemode.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author einTosti
 */
public class ColourInventory {
    private static ColourInventory instance;

    public static synchronized ColourInventory getInstance() {
        if (instance == null) {
            instance = new ColourInventory();
        }
        return instance;
    }

    private Inventory getColourInventory() {
        Inventory inv = Bukkit.createInventory(null, 36, Utils.getInstance().getString("colour_guiName"));

        addColourItem(inv, 10, 14, ChatColor.RED, Utils.getInstance().getString("colour_red"));
        addColourItem(inv, 11, 1, ChatColor.GOLD, Utils.getInstance().getString("colour_orange"));
        addColourItem(inv, 12, 4, ChatColor.YELLOW, Utils.getInstance().getString("colour_yellow"));
        addColourItem(inv, 13, 6, ChatColor.LIGHT_PURPLE, Utils.getInstance().getString("colour_pink"));
        addColourItem(inv, 14, 10, ChatColor.DARK_PURPLE, Utils.getInstance().getString("colour_purple"));
        addColourItem(inv, 15, 5, ChatColor.GREEN, Utils.getInstance().getString("colour_lime"));
        addColourItem(inv, 16, 13, ChatColor.DARK_GREEN, Utils.getInstance().getString("colour_green"));
        addColourItem(inv, 19, 11, ChatColor.BLUE, Utils.getInstance().getString("colour_blue"));
        addColourItem(inv, 20, 9, ChatColor.DARK_AQUA, Utils.getInstance().getString("colour_cyan"));
        addColourItem(inv, 21, 3, ChatColor.AQUA, Utils.getInstance().getString("colour_aqua"));
        addColourItem(inv, 22, 0, ChatColor.WHITE, Utils.getInstance().getString("colour_white"));
        addColourItem(inv, 23, 8, ChatColor.GRAY, Utils.getInstance().getString("colour_lightGrey"));
        addColourItem(inv, 24, 7, ChatColor.DARK_GRAY, Utils.getInstance().getString("colour_grey"));
        addColourItem(inv, 25, 15, ChatColor.BLACK, Utils.getInstance().getString("colour_black"));

        for (int i = 0; i <= 9; ++i) {
            Utils.getInstance().addItemStack(inv, i, Material.STAINED_GLASS_PANE, 15, " ");
        }
        Utils.getInstance().addItemStack(inv, 17, Material.STAINED_GLASS_PANE, 15, " ");
        Utils.getInstance().addItemStack(inv, 18, Material.STAINED_GLASS_PANE, 15, " ");
        for (int i = 26; i <= 35; ++i) {
            Utils.getInstance().addItemStack(inv, i, Material.STAINED_GLASS_PANE, 15, " ");
        }
        return inv;
    }

    public void openInventory(Player player) {
        player.openInventory(getColourInventory());
    }

    private void addColourItem(Inventory inv, int position, int id, ChatColor chatColor, String displayName) {
        if (chatColor.equals(Utils.getInstance().mColour)) {
            addSelectedColour(inv, position, id, displayName);
        } else {
            Utils.getInstance().addItemStack(inv, position, Material.WOOL, id, displayName);
        }
    }

    private void addSelectedColour(Inventory inv, int position, int id, String displayName) {
        ItemStack itemStack = new ItemStack(Material.WOOL, 1, (byte) id);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(displayName);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(meta);
        inv.setItem(position, itemStack);
    }
}
