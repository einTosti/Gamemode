package de.eintosti.gamemode.misc;

import de.eintosti.gamemode.Gamemode;
import de.eintosti.gamemode.inventories.ColourInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author einTosti
 */
public class Utils {
    public final String FILENAME = "colour.yml";
    private final String COLOUR_KEY = "messageColour";
    private final String KEY_VALUE_SEP = ":";

    public ChatColor mColour = ChatColor.GRAY;
    public File mGameMode;
    public File mColourFile;

    private static Utils instance;

    public static synchronized Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public String getString(String string) {
        try {
            return Messages.getInstance().mMessageData.get(string).replace("%prefix%", Messages.getInstance().mMessageData.get("prefix").replace("%colour%", mColour.toString()).replace("&", "§"));
        } catch (NullPointerException e) {
            Messages.getInstance().createMessageFile();
            return getString(string);
        }
    }

    /*
     * GUIs
     */
    public void addItemStack(Inventory inv, int position, Material material, int id, String displayName) {
        ItemStack itemStack = new ItemStack(material, 1, (byte) id);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        itemStack.setItemMeta(meta);
        inv.setItem(position, itemStack);
    }

    public void showPermErrorMessage(Player player) {
        player.sendMessage(getString("no_permission"));
    }

    /*
     * Manage Colour
     */
    public void setColour(Player player, ChatColor chatColor, String colour) {
        mColour = chatColor;
        saveColour();

        ColourInventory.getInstance().openInventory(player);
        Gamemode.plugin.getLogger().info(getString("colour_changed"));
        player.sendMessage(getString("colour_changed").replace("%colour%", mColour + colour));
    }

    public void saveColour() {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            ArrayList<String> lines = new ArrayList<>();

            if (mColourFile.exists()) {
                InputStream fis = new FileInputStream(mColourFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                br = new BufferedReader(isr);

                while (true) {
                    String line = br.readLine();
                    if (line == null || line.startsWith(COLOUR_KEY)) {
                        break;
                    }
                    lines.add(line);
                }
            }
            OutputStream fos = new FileOutputStream(mColourFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));

            bw = new BufferedWriter(osw);
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }

            bw.write(COLOUR_KEY);
            bw.write(KEY_VALUE_SEP);
            bw.write(mColour.getChar());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readColour() {
        BufferedReader br = null;
        try {
            if (mColourFile.exists()) {
                InputStream fis = new FileInputStream(mColourFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                br = new BufferedReader(isr);

                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }

                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    String[] keyValues = line.split(":");

                    if (keyValues[0].equals(COLOUR_KEY)) {
                        mColour = ChatColor.getByChar(keyValues[1].charAt(0));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(mColourFile, "r");
            while (true) {
                String line = raf.readLine();
                if (line == null) {
                    break;
                }
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
