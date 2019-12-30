package de.eintosti.gamemode.inventories;

import de.eintosti.gamemode.misc.Utils;
import de.eintosti.gamemode.misc.external.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

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
        UUID uuid = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(null, 27, Utils.getInstance().getString("gamemode_guiName", uuid));

        addCurrentGamemodeItem(player, GameMode.SURVIVAL, inventory, 10, Utils.getInstance().getString("gamemode_survival", uuid));
        addCurrentGamemodeItem(player, GameMode.CREATIVE, inventory, 12, Utils.getInstance().getString("gamemode_creative", uuid));
        addCurrentGamemodeItem(player, GameMode.ADVENTURE, inventory, 14, Utils.getInstance().getString("gamemode_adventure", uuid));
        addCurrentGamemodeItem(player, GameMode.SPECTATOR, inventory, 16, Utils.getInstance().getString("gamemode_spectator", uuid));

        for (int i = 0; i <= 9; ++i) {
            Utils.getInstance().addGlassPane(inventory, i);
        }
        for (int i = 17; i <= 26; ++i) {
            Utils.getInstance().addGlassPane(inventory, i);
        }
        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(getGamemodeInventory(player));
    }

    private void addGameModeItemStack(Inventory inventory, Player player, int position, XMaterial material, String displayName, String gameMode) {
        ItemStack itemStack = material.parseItem();
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList("", Utils.getInstance().getString("gamemode_lore", player.getUniqueId()).replace("%gamemode%", gameMode)));
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(position, itemStack);
    }

    private void addCurrentGamemodeItem(Player player, GameMode gameMode, Inventory inv, int position, String displayName) {
        if (player.getGameMode().equals(gameMode)) {
            addGameModeItemStack(inv, player, position, XMaterial.ENCHANTED_BOOK, Utils.getInstance().getColour(player.getUniqueId()).getAsString() + displayName, displayName);
        } else if (!player.getGameMode().equals(gameMode)) {
            addGameModeItemStack(inv, player, position, XMaterial.BOOK, Utils.getInstance().getColour(player.getUniqueId()).getAsString() + displayName, displayName);
        }
    }
}
