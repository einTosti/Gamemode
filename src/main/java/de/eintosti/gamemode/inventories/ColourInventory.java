package de.eintosti.gamemode.inventories;

import de.eintosti.gamemode.misc.Colour;
import de.eintosti.gamemode.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

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

    private Inventory getColourInventory(Player player) {
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(null, 36, Utils.getInstance().getString("colour_guiName", uuid));

        addColourItem(inventory, player, 10, Colour.RED, Utils.getInstance().getString("colour_red", uuid));
        addColourItem(inventory, player, 11, Colour.ORANGE, Utils.getInstance().getString("colour_orange", uuid));
        addColourItem(inventory, player, 12, Colour.YELLOW, Utils.getInstance().getString("colour_yellow", uuid));
        addColourItem(inventory, player, 13, Colour.PINK, Utils.getInstance().getString("colour_pink", uuid));
        addColourItem(inventory, player, 14, Colour.PURPLE, Utils.getInstance().getString("colour_purple", uuid));
        addColourItem(inventory, player, 15, Colour.LIME, Utils.getInstance().getString("colour_lime", uuid));
        addColourItem(inventory, player, 16, Colour.GREEN, Utils.getInstance().getString("colour_green", uuid));
        addColourItem(inventory, player, 19, Colour.BLUE, Utils.getInstance().getString("colour_blue", uuid));
        addColourItem(inventory, player, 20, Colour.CYAN, Utils.getInstance().getString("colour_cyan", uuid));
        addColourItem(inventory, player, 21, Colour.LIGHT_BLUE, Utils.getInstance().getString("colour_aqua", uuid));
        addColourItem(inventory, player, 22, Colour.WHITE, Utils.getInstance().getString("colour_white", uuid));
        addColourItem(inventory, player, 23, Colour.LIGHT_GREY, Utils.getInstance().getString("colour_lightGrey", uuid));
        addColourItem(inventory, player, 24, Colour.GREY, Utils.getInstance().getString("colour_grey", uuid));
        addColourItem(inventory, player, 25, Colour.BLACK, Utils.getInstance().getString("colour_black", uuid));

        addGlassPanes(inventory);
        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(getColourInventory(player));
    }

    private void addColourItem(Inventory inventory, Player player, int position, Colour colour, String displayName) {
        if (colour.equals(Utils.getInstance().getColour(player.getUniqueId()))) {
            addSelectedColour(inventory, player, position, displayName);
        } else {
            Utils.getInstance().addItemStack(inventory, position, Utils.getInstance().getWoolFromColour(colour), displayName);
        }
    }

    private void addSelectedColour(Inventory inventory, Player player, int position, String displayName) {
        ItemStack itemStack = Utils.getInstance().getColouredWool(player.getUniqueId()).parseItem();
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(itemMeta);
        inventory.setItem(position, itemStack);
    }

    private void addGlassPanes(Inventory inventory) {
        for (int i = 0; i <= 9; ++i) {
            Utils.getInstance().addGlassPane(inventory, i);
        }
        Utils.getInstance().addGlassPane(inventory, 17);
        Utils.getInstance().addGlassPane(inventory, 18);
        for (int i = 26; i <= 35; ++i) {
            Utils.getInstance().addGlassPane(inventory, i);
        }
    }
}
