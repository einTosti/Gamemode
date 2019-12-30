package de.eintosti.gamemode.misc.external;

import com.google.common.base.Enums;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/*
 * References
 *
 * * * GitHub: https://github.com/CryptoMorin/XSeries/blob/master/XMaterial.java
 * * XSeries: https://www.spigotmc.org/threads/378136/
 * Pre-flattening: https://minecraft.gamepedia.com/Java_Edition_data_values/Pre-flattening
 * Materials: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
 * Materials (1.8): https://helpch.at/docs/1.8/org/bukkit/Material.html
 * Material IDs: https://minecraft-ids.grahamedgecombe.com/
 * Material Source Code: https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Material.java
 * XMaterial v1: https://www.spigotmc.org/threads/329630/
 */

/**
 * <b>XMaterial</b> - Data Values/Pre-flattening<br>
 * Supports 1.8-1.15<br>
 * 1.13 and above as priority.
 *
 * @author Crypto Morin
 * @version 3.1.3
 * @see Material
 * @see ItemStack
 */
public enum XMaterial {
    ANVIL,
    BLACK_STAINED_GLASS_PANE(15, "STAINED_GLASS_PANE"),
    BLACK_WOOL(15, "WOOL"),
    BLUE_WOOL(11, "WOOL"),
    BOOK,
    CYAN_WOOL(9, "WOOL"),
    ENCHANTED_BOOK,
    GRAY_WOOL(7, "WOOL"),
    GREEN_WOOL(13, "WOOL"),
    LIGHT_BLUE_WOOL(3, "WOOL"),
    LIGHT_GRAY_WOOL(8, "WOOL"),
    LIME_WOOL(5, "WOOL"),
    ORANGE_WOOL(1, "WOOL"),
    PINK_WOOL(6, "WOOL"),
    PLAYER_HEAD(3, "SKULL", "SKULL_ITEM"),
    PURPLE_WOOL(10, "WOOL"),
    RED_WOOL(14, "WOOL"),
    WHITE_WOOL("WOOL"),
    YELLOW_WOOL(4, "WOOL");

    /**
     * An immutable cached set of {@link XMaterial#values()} to avoid allocating memory for
     * calling the method every time.
     *
     * @since 2.0.0
     */
    public static final EnumSet<XMaterial> VALUES = EnumSet.allOf(XMaterial.class);

    /**
     * Guava (Google Core Libraries for Java)'s cache for performance and timed caches.
     * For XMaterials that are already parsed once.
     *
     * @since 3.0.0
     */
    private static final Cache<XMaterial, Material> PARSED_CACHE = CacheBuilder.newBuilder()
            .softValues()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();

    /**
     * Pre-compiled RegEx pattern.
     * Include both replacements to avoid recreating string multiple times with multiple RegEx checks.
     *
     * @since 3.0.0
     */
    private static final Pattern FORMAT_PATTERN = Pattern.compile("\\W+");
    /**
     * The current version of the server in the a form of a major {@link MinecraftVersion} version.
     *
     * @since 1.0.0
     */
    private static final MinecraftVersion VERSION = valueOfVersion(Bukkit.getVersion());
    /**
     * Cached result if the server version is after the flattening ({@link MinecraftVersion#V1_13}) update.
     * Please don't mistake this with flat-chested people. It happened.
     *
     * @since 3.0.0
     */
    private static final boolean ISFLAT = isVersionOrHigher(MinecraftVersion.V1_13);

    /**
     * The data value of this material https://minecraft.gamepedia.com/Java_Edition_data_values/Pre-flattening
     */
    private final byte data;
    /**
     * A list of material names that was being used for older verions.
     */
    private final String[] legacy;

    XMaterial(int data, String... legacy) {
        this.data = (byte) data;
        this.legacy = legacy;
    }

    XMaterial() {
        this(0);
    }

    XMaterial(String... legacy) {
        this(0, legacy);
    }

    /**
     * Checks if the version is {@link MinecraftVersion#V1_13} (Aquatic Update) or higher.
     * An invocation of this method yields the cached result from the expression:
     * <p>
     * <blockquote>
     * {@link #isVersionOrHigher(MinecraftVersion V1_13)}
     * </blockquote>
     *
     * @return true if 1.13 or higher.
     * @see #getVersion()
     * @see #isVersionOrHigher(MinecraftVersion)
     * @since 1.0.0
     */
    public static boolean isNewVersion() {
        return ISFLAT;
    }

    /**
     * This is just an extra method that method that can be used for many cases.
     * It can be used in {@link org.bukkit.event.player.PlayerInteractEvent}
     * or when accessing {@link org.bukkit.entity.Player#getMainHand()},
     * or other compatibility related methods.
     * <p>
     * An invocation of this method yields exactly the same result as the expression:
     * <p>
     * <blockquote>
     * {@link #getVersion()} == {@link MinecraftVersion#V1_8}
     * </blockquote>
     *
     * @since 2.0.0
     */
    public static boolean isOneEight() {
        return VERSION == MinecraftVersion.V1_8;
    }

    /**
     * The current version of the server.
     *
     * @return the current server version or {@link MinecraftVersion#UNKNOWN} if unknown or below 1.8.
     * @see #isNewVersion()
     * @since 2.0.0
     */
    @Nonnull
    public static MinecraftVersion getVersion() {
        return VERSION;
    }

    /**
     * Attempts to build the string like an enum name.
     * Removes all the spaces, numbers and extra non-English characters. Also removes some config/in-game based strings.
     *
     * @param name the material name to modify.
     * @return a Material enum name.
     * @since 2.0.0
     */
    @Nonnull
    private static String format(@Nonnull String name) {
        return FORMAT_PATTERN.matcher(
                name.trim().replace('-', '_').replace(' ', '_')).replaceAll("").toUpperCase(Locale.ENGLISH);
    }

    /**
     * Parses the material name if the legacy name has a version attached to it.
     *
     * @param name the material name to parse.
     * @return the material name with the version removed.
     * @since 2.0.0
     */
    @Nonnull
    private static String parseLegacyMaterialName(String name) {
        int index = name.indexOf('/');
        return index == -1 ? name : name.substring(0, index);
    }

    /**
     * Checks if the specified version is the same version or higher than the current server version.
     *
     * @param version the version to be checked.
     * @return true of the version is equal or higher than the current version.
     * @since 2.0.0
     */
    public static boolean isVersionOrHigher(@Nonnull MinecraftVersion version) {
        Objects.requireNonNull(version, "Cannot compare to a null version");
        return VERSION.ordinal() >= version.ordinal();
    }

    /**
     * Converts the enum names to a more friendly and readable string.
     *
     * @return a formatted string.
     * @see #toWord(String)
     * @since 2.1.0
     */
    @Nonnull
    public static String toWord(@Nonnull Material material) {
        Objects.requireNonNull(material, "Cannot translate a null material to a word");
        return toWord(material.name());
    }

    /**
     * Parses an enum name to a normal word.
     * Normal names have underlines removed and each word capitalized.
     * <p>
     * <b>Examples:</b>
     * <pre>
     *     EMERALD                 -> Emerald
     *     EMERALD_BLOCK           -> Emerald Block
     *     ENCHANTED_GOLDEN_APPLE  -> Enchanted Golden Apple
     * </pre>
     *
     * @param name the name of the enum.
     * @return a cleaned more readable enum name.
     * @since 2.1.0
     */
    @Nonnull
    private static String toWord(@Nonnull String name) {
        return WordUtils.capitalize(name.replace('_', ' ').toLowerCase(Locale.ENGLISH));
    }

    /**
     * Gets the exact major version (..., 1.9, 1.10, ..., 1.14)
     *
     * @param version Supports {@link Bukkit#getVersion()}, {@link Bukkit#getBukkitVersion()} and normal formats such as "1.14"
     * @return the exact major version, or {@link MinecraftVersion#UNKNOWN} if unknown or unsupported.
     * @since 2.0.0
     */
    @Nonnull
    public static String getMajorVersion(@Nonnull String version) {
        Validate.notEmpty(version, "Cannot get exact major minecraft version for null or empty version");

        // getBukkitVersion()
        if (version.contains("-R") || version.endsWith("SNAPSHOT"))
            version = version.substring(0, version.indexOf('-'));

        // getVersion()
        int index = version.indexOf("MC:");
        if (index != -1) version = version.substring(index + 4, version.length() - 1);

        // 1.13.2, 1.14.4, etc...
        int lastDot = version.lastIndexOf('.');
        if (version.indexOf('.') != lastDot) version = version.substring(0, lastDot);
        return version;
    }

    /**
     * Parses the string arugment to a version.
     * Supports {@link Bukkit#getVersion()}, {@link Bukkit#getBukkitVersion()} and normal formats such as "1.14"
     *
     * @param version the server version.
     * @return the Minecraft version represented by the string.
     * @since 2.0.0
     */
    @Nonnull
    public static MinecraftVersion valueOfVersion(@Nonnull String version) {
        Validate.notEmpty(version, "Cannot get minecraft version for null or empty version");

        version = getMajorVersion(version);
        if (version.equals("1.10") || version.equals("1.11") || version.equals("1.12")) return MinecraftVersion.V1_9;

        version = 'V' + version.replace('.', '_');
        return Enums.getIfPresent(MinecraftVersion.class, version).or(MinecraftVersion.UNKNOWN);
    }

    /**
     * User-friendly readable name for this material
     * In most cases you should be using {@link #name()} instead.
     *
     * @return string of this object.
     * @see #toWord(String)
     * @since 3.0.0
     */
    @Override
    public String toString() {
        return toWord(this.name());
    }

    /**
     * Parses an item from this XMaterial.
     * Uses data values on older versions.
     *
     * @return an ItemStack with the same material (and data value if in older versions.)
     * @see #parseItem(boolean)
     * @since 1.0.0
     */
    @Nullable
    public ItemStack parseItem() {
        return parseItem(false);
    }

    /**
     * Parses an item from this XMaterial.
     * Uses data values on older versions.
     *
     * @param suggest if true {@link #parseMaterial(boolean true)} will be used.
     * @return an ItemStack with the same material (and data value if in older versions.)
     * @since 2.0.0
     */
    @Nullable
    @SuppressWarnings("deprecation")
    public ItemStack parseItem(boolean suggest) {
        Material material = this.parseMaterial(suggest);
        if (material == null) return null;
        return ISFLAT ? new ItemStack(material) : new ItemStack(material, 1, this.data);
    }

    /**
     * Parses the material of this XMaterial.
     *
     * @return the material related to this XMaterial based on the server version.
     * @see #parseMaterial(boolean)
     * @since 1.0.0
     */
    @Nullable
    public Material parseMaterial() {
        return parseMaterial(false);
    }


    @Nullable
    public Material parseMaterial(boolean suggest) {
        Material mat = PARSED_CACHE.getIfPresent(this);
        if (mat != null) return mat;

        mat = Material.getMaterial(this.name());
        if (mat == null) mat = requestOldMaterial(suggest);

        if (mat != null) PARSED_CACHE.put(this, mat);
        return mat;
    }

    /**
     * Parses a material for older versions of Minecraft.
     * Accepts suggestions if specified.
     *
     * @param suggest if true suggested materials will be considered for old versions.
     * @return a parsed material suitable for the current Minecraft version.
     * @see #parseMaterial(boolean)
     * @since 2.0.0
     */
    @Nullable
    private Material requestOldMaterial(boolean suggest) {
        boolean noMaterialParse = this.isNew() && !suggest;
        Material material;

        for (int i = this.legacy.length - 1; i >= 0; i--) {
            String legacy = this.legacy[i];

            // Slash means it's just another name for the material in another version.
            int index = legacy.indexOf('/');
            if (index != -1) {
                legacy = legacy.substring(0, index);
                material = Material.getMaterial(legacy);

                if (material != null) return material;
                else continue;
            }

            // According to the suggestion format list, all the other names continuing
            // from here are considered as a "suggestion" if there's no slash anymore.
            // But continue if it's not even a new material.
            if (noMaterialParse) return null;
            material = Material.getMaterial(legacy);
            if (material != null) return material;
        }
        return null;
    }

    /**
     * Checks if the material is newly added after the 1.13 Aquatic Update ({@link MinecraftVersion#V1_13}).
     *
     * @return true if the material was newly added, otherwise false.
     * @since 2.0.0
     */
    public boolean isNew() {
        return this.legacy.length != 0 && this.legacy[0].charAt(1) == '.';
    }

    /**
     * Only major Minecraft versions related to most changes.
     * The enum's order should not be changed.
     *
     * @since 2.0.0
     */
    public enum MinecraftVersion {
        /**
         * 1.7 or below.
         * https://minecraft.gamepedia.com/Java_Edition_1.7
         *
         * @since 2.0.0
         */
        UNKNOWN,

        /**
         * Bountiful Update
         * https://minecraft.gamepedia.com/Java_Edition_1.18
         *
         * @since 2.0.0
         */
        V1_8,

        /**
         * Combat Update (Pitiful Update? 90% of the reason why that this class is a thing)
         * https://minecraft.gamepedia.com/Java_Edition_1.9
         *
         * @since 2.0.0
         */
        V1_9,

        /**
         * Aquatic Update
         * Includes 1.10, 1.11 and 1.12
         * https://minecraft.gamepedia.com/Java_Edition_1.13
         *
         * @since 2.0.0
         */
        V1_13,

        /**
         * Village Pillage Update
         * https://minecraft.gamepedia.com/Java_Edition_1.14
         *
         * @since 2.0.0
         */
        V1_14,

        /**
         * Buzzy Bees Update
         * https://minecraft.gamepedia.com/Java_Edition_1.15
         *
         * @since 3.0.0
         */
        V1_15
    }
}