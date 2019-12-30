package de.eintosti.gamemode.misc;

import org.bukkit.ChatColor;

/**
 * @author einTosti
 */
public enum Colour {
    RED(14, ChatColor.RED, "§c"), ORANGE(1, ChatColor.GOLD, "§6"), YELLOW(4, ChatColor.YELLOW, "§e"), PINK(6, ChatColor.LIGHT_PURPLE, "§d"), PURPLE(10, ChatColor.DARK_PURPLE, "§5"), LIME(5, ChatColor.GREEN, "§a"), GREEN(13, ChatColor.DARK_GREEN, "§2"), BLUE(11, ChatColor.BLUE, "§9"), CYAN(9, ChatColor.DARK_AQUA, "§3"), LIGHT_BLUE(3, ChatColor.AQUA, "§b"), WHITE(0, ChatColor.WHITE, "§f"), GREY(8, ChatColor.DARK_GRAY, "§8"), LIGHT_GREY(7, ChatColor.GRAY, "§7"), BLACK(15, ChatColor.BLACK, "§0");

    private int id;
    private ChatColor chatColor;
    private String colourString;

    Colour(int id, ChatColor chatColor, String colourString) {
        this.id = id;
        this.chatColor = chatColor;
        this.colourString = colourString;
    }

    public int getId() {
        return id;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getAsString() {
        return colourString;
    }

    public static Colour parseColour(ChatColor chatColor) {
        for (Colour colour : Colour.values()) {
            if (colour.getChatColor() == chatColor) return colour;
        }
        return null;
    }

    public static Colour parseColour(char c) {
        for (Colour colour : Colour.values()) {
            if (colour.getId() == c) return colour;
        }
        return null;
    }
}
