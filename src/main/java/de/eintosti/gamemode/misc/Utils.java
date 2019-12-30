package de.eintosti.gamemode.misc;

import de.eintosti.gamemode.Gamemode;
import de.eintosti.gamemode.inventories.ColourInventory;
import de.eintosti.gamemode.misc.external.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author einTosti
 */
public class Utils {
    public final static String FILENAME = "colour.data";

    public HashMap<UUID, Colour> playerColour = new HashMap<>();
    public File gameMode;

    private static Utils instance;

    public static synchronized Utils getInstance() {
        if (instance == null) instance = new Utils();
        return instance;
    }

    public String getString(String string, UUID uuid) {
        try {
            if (uuid == null) {
                return Messages.getInstance().mMessageData.get(string).replace("%prefix%", Messages.getInstance().mMessageData.get("prefix").replace("%colour%", Colour.RED.getAsString())).replace("&", "§");
            }
            return Messages.getInstance().mMessageData.get(string).replace("%prefix%", Messages.getInstance().mMessageData.get("prefix").replace("%colour%", getColour(uuid).getAsString())).replace("&", "§");
        } catch (NullPointerException e) {
            Messages.getInstance().createMessageFile();
            return getString(string, uuid);
        }
    }

    public Colour getColour(UUID uuid) {
        if (!playerColour.containsKey(uuid)) {
            playerColour.put(uuid, Colour.LIGHT_BLUE);
        }
        return playerColour.get(uuid);
    }

    public void setColour(Player player, Colour colour, String colourString) {
        UUID uuid = player.getUniqueId();
        playerColour.put(uuid, colour);
        ColourInventory.getInstance().openInventory(player);
        player.sendMessage(getString("colour_changed", uuid).replace("%colour%", getColour(uuid).getAsString() + colourString));
    }

    public void showPermErrorMessage(Player player) {
        player.sendMessage(getString("no_permission", player.getUniqueId()));
    }

    public void addItemStack(Inventory inventory, int position, XMaterial material, String displayName) {
        ItemStack itemStack = material.parseItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(position, itemStack);
    }

    public void addGlassPane(Inventory inventory, int position) {
        ItemStack itemStack = XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(position, itemStack);
    }

    public XMaterial getWoolFromColour(Colour colour) {
        switch (colour) {
            case RED:
                return XMaterial.RED_WOOL;
            case ORANGE:
                return XMaterial.ORANGE_WOOL;
            case YELLOW:
                return XMaterial.YELLOW_WOOL;
            case PINK:
                return XMaterial.PINK_WOOL;
            case PURPLE:
                return XMaterial.PURPLE_WOOL;
            case LIME:
                return XMaterial.LIME_WOOL;
            case GREEN:
                return XMaterial.GREEN_WOOL;
            case BLUE:
                return XMaterial.BLUE_WOOL;
            case CYAN:
                return XMaterial.CYAN_WOOL;
            case WHITE:
                return XMaterial.WHITE_WOOL;
            case LIGHT_GREY:
                return XMaterial.LIGHT_GRAY_WOOL;
            case GREY:
                return XMaterial.GRAY_WOOL;
            case BLACK:
                return XMaterial.BLACK_WOOL;
            default:
                return XMaterial.LIGHT_BLUE_WOOL;
        }
    }

    public XMaterial getColouredWool(UUID uuid) {
        return getWoolFromColour(getColour(uuid));
    }

    public void freeze() {
        File colourFile = new File(Gamemode.plugin.getDataFolder(), FILENAME);

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(colourFile));
            oos.writeObject(playerColour);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void thaw() {
        File colourFile = new File(Gamemode.plugin.getDataFolder(), FILENAME);
        if (!colourFile.exists()) return;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(colourFile));
            this.playerColour = (HashMap<UUID, Colour>) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            thaw();
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
