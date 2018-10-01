package de.eintosti.gamemode.misc;

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
        if (instance == null) instance = new Utils();
        return instance;
    }

    public String getString(String string) {
        try {
            return Messages.getInstance().mMessageData.get(string).replace("%prefix%", Messages.getInstance().mMessageData.get("prefix").replace("%colour%", mColour.toString())).replace("&", "§");
        } catch (NullPointerException e) {
            Messages.getInstance().createMessageFile();
            return getString(string);
        }
    }

    public void showPermErrorMessage(Player player) {
        player.sendMessage(getString("no_permission"));
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

    /*
     * Manage Colour
     */
    public void setColour(Player player, ChatColor chatColor, String colour) {
        mColour = chatColor;
        saveColour();

        ColourInventory.getInstance().openInventory(player);
        player.sendMessage(getString("colour_changed").replace("%colour%", mColour + colour));
    }

    public void saveColour() {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            ArrayList<String> lines = new ArrayList<>();
            if (mColourFile.exists()) {
                InputStream fis = new FileInputStream(mColourFile);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                bufferedReader = new BufferedReader(isr);

                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null || line.startsWith(COLOUR_KEY)) break;
                    lines.add(line);
                }
            }
            OutputStream outputStream = new FileOutputStream(mColourFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));

            bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.write(COLOUR_KEY);
            bufferedWriter.write(KEY_VALUE_SEP);
            bufferedWriter.write(mColour.getChar());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readColour() {
        BufferedReader bufferedReader = null;
        try {
            if (mColourFile.exists()) {
                InputStream inputStream = new FileInputStream(mColourFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                bufferedReader = new BufferedReader(inputStreamReader);
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) break;
                    if (line.isEmpty() || line.startsWith("#")) continue;
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
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(mColourFile, "r");
            while (true) {
                String line = randomAccessFile.readLine();
                if (line == null) break;
                if (line.isEmpty() || line.startsWith("#")) continue;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
