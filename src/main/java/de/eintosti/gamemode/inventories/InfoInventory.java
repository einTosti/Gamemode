package de.eintosti.gamemode.inventories;

import de.eintosti.gamemode.Gamemode;
import de.eintosti.gamemode.misc.Utils;
import de.eintosti.gamemode.misc.external.XMaterial;
import org.bukkit.Bukkit;
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

    private Inventory getInfoInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8Plugin Information");

        addSkullItemStack(inventory, player);
        addVersionItemStack(inventory, player);

        for (int i = 0; i <= 11; ++i) {
            Utils.getInstance().addGlassPane(inventory, i);
        }
        for (int i = 15; i <= 26; ++i) {
            Utils.getInstance().addGlassPane(inventory, i);
        }
        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(getInfoInventory(player));
    }

    private void addVersionItemStack(Inventory inventory, Player player) {
        PluginDescriptionFile pdf = Gamemode.plugin.getDescription();
        ItemStack itemStack = XMaterial.ANVIL.parseItem();

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§7Current Version");
        itemMeta.setLore(Arrays.asList("§8» " + Utils.getInstance().getColour(player.getUniqueId()).getAsString() + pdf.getVersion()));
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(14, itemStack);
    }

    @SuppressWarnings("deprecation")
    private void addSkullItemStack(Inventory inventory, Player player) {
        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.setOwner("einTosti");
        skullMeta.setDisplayName("§7Plugin Author");
        skullMeta.setLore(Arrays.asList(("§8» " + Utils.getInstance().getColour(player.getUniqueId()).getAsString() + "einTosti")));
        skull.setItemMeta(skullMeta);

        inventory.setItem(12, skull);
    }
}
