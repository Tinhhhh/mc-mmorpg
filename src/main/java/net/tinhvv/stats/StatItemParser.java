package net.tinhvv.stats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatItemParser {
    private static final Pattern PATTERN = Pattern.compile("([a-zA-Z_ ]+)\\s([-+]?\\d+(\\.\\d+)?)");

    public static List<StatModifier> parse(ItemStack item, String sourceId) {
        List<StatModifier> list = new ArrayList<>();
        if (item == null || !item.hasItemMeta()) return list;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return list;

        for (String rawLine : meta.getLore()) {
            String line = ChatColor.stripColor(rawLine).trim();

            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) continue;

            String statName = matcher.group(1).trim().toUpperCase().replace(" ", "_");
            double value = Double.parseDouble(matcher.group(2));

            try {
                StatType type = StatType.valueOf(statName);
                list.add(new StatModifier(type, value, sourceId));
            } catch (IllegalArgumentException ignored) {
            }
        }

        return list;
    }

    private static boolean isWeapon(ItemStack item) {
        Material type = item.getType();

        // Danh sách các loại vũ khí vanilla
        if (type.name().endsWith("_SWORD") ||
                type.name().endsWith("_AXE") ||
                type.name().endsWith("_BOW") ||
                type.name().endsWith("_CROSSBOW") ||
                type.name().endsWith("_TRIDENT") ||
                type.name().endsWith("_HOE")) {
            return true;
        }

        // Nếu là item custom, kiểm tra PersistentData hoặc key đặc biệt trong lore
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore()) {
            for (String line : meta.getLore()) {
                if (ChatColor.stripColor(line).toLowerCase().contains("weapon")) {
                    return true;
                }
            }
        }

        return false;
    }

}

