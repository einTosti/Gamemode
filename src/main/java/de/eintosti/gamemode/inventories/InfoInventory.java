package de.eintosti.gamemode.inventories;

import de.eintosti.gamemode.Gamemode;
import de.eintosti.gamemode.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Arrays;

/**
 * @author einTosti
 */
public class InfoInventory {
    private static InfoInventory instance;

    public static synchronized InfoInventory getInstance() {
        if (instance == null) {
            instance = new InfoInventory();
        }
        return instance;
    }

    private Inventory getInfoInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Plugin Information");

        this.addSkullItemStack(inv);
        this.addVersionItemStack(inv);

        for (int i = 0; i <= 9; ++i) {
            Utils.getInstance().addItemStack(inv, i, Material.STAINED_GLASS_PANE, 15, " ");
        }
        for (int i = 17; i <= 26; ++i) {
            Utils.getInstance().addItemStack(inv, i, Material.STAINED_GLASS_PANE, 15, " ");
        }
        return inv;
    }

    public void openInventory(Player player) {
        player.openInventory(getInfoInventory());
    }

    private void addVersionItemStack(Inventory inv) {
        PluginDescriptionFile pdf = Gamemode.plugin.getDescription();
        ItemStack itemStack = new ItemStack(Material.DIODE, 1, (short) 0);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§7Current Version");
        itemMeta.setLore(Arrays.asList("§8» " + Utils.getInstance().mColour + pdf.getVersion()));
        itemStack.setItemMeta(itemMeta);

        inv.setItem(14, itemStack);
    }

    private void addSkullItemStack(Inventory inv) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.setOwner("einTosti");
        skullMeta.setDisplayName("§7Plugin Author");
        skullMeta.setLore(Arrays.asList(("§8» " + Utils.getInstance().mColour + "einTosti")));
        skull.setItemMeta(skullMeta);

        inv.setItem(12, skull);
    }
}
