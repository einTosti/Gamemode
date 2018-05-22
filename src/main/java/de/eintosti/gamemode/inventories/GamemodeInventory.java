package de.eintosti.gamemode.inventories;

import de.eintosti.gamemode.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * @author einTosti
 */
public class GamemodeInventory {
    private static GamemodeInventory instance;

    public static synchronized GamemodeInventory getInstance() {
        if (instance == null) {
            instance = new GamemodeInventory();
        }
        return instance;
    }

    private Inventory getGamemodeInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Utils.getInstance().getString("gamemode_guiName"));

        addCurrentGamemodeItem(player, GameMode.SURVIVAL, inv, 10, Utils.getInstance().getString("gamemode_survival"));
        addCurrentGamemodeItem(player, GameMode.CREATIVE, inv, 12, Utils.getInstance().getString("gamemode_creative"));
        addCurrentGamemodeItem(player, GameMode.ADVENTURE, inv, 14, Utils.getInstance().getString("gamemode_adventure"));
        addCurrentGamemodeItem(player, GameMode.SPECTATOR, inv, 16, Utils.getInstance().getString("gamemode_spectator"));

        for (int i = 0; i <= 9; ++i) {
            Utils.getInstance().addItemStack(inv, i, Material.STAINED_GLASS_PANE, 15, " ");
        }
        for (int i = 17; i <= 26; ++i) {
            Utils.getInstance().addItemStack(inv, i, Material.STAINED_GLASS_PANE, 15, " ");
        }
        return inv;
    }

    public void openInventory(Player player) {
        player.openInventory(getGamemodeInventory(player));
    }

    private void addGameModeItemStack(Inventory inv, int position, Material material, String displayName, String gameMode) {
        ItemStack is = new ItemStack(material, 1, (byte) 0);
        ItemMeta itemMeta = is.getItemMeta();
        itemMeta.setDisplayName(displayName);

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.getInstance().getString("gamemode_lore").replace("%gamemode%", gameMode));

        itemMeta.setLore(lore);
        is.setItemMeta(itemMeta);
        inv.setItem(position, is);
    }

    private void addCurrentGamemodeItem(Player player, GameMode gameMode, Inventory inv, int position, String displayName) {
        if (player.getGameMode().equals(gameMode)) {
            addGameModeItemStack(inv, position, Material.ENCHANTED_BOOK, Utils.getInstance().mColour + displayName, displayName);
        } else if (!player.getGameMode().equals(gameMode)) {
            addGameModeItemStack(inv, position, Material.BOOK, Utils.getInstance().mColour + displayName, displayName);
        }
    }
}
