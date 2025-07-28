package net.tinhvv.stats;

import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatItemParser {
    private static final Logger LOGGER = Logger.getLogger(StatItemParser.class.getName());

    public static String getItemSource(ItemStack item) {
        Material type = item.getType();

        // Danh sách các loại vũ khí vanilla
        String finalResult = "VANILLA:" + type.toString().toUpperCase();

        // Kiểm tra xem item có tag là vũ khí không
        String tagName = Mmorpg.getCustomItemManager().getTagName(item).toUpperCase();
        if (!Mmorpg.getCustomItemManager().getTagName(item).equals("UNKNOWN")) {
            finalResult = "CUSTOM:" + tagName;
        }

        return finalResult;
    }

    public List<StatModifier> merge(List<StatModifier> list1, List<StatModifier> list2) {
        Map<StatType, Double> merged = new EnumMap<>(StatType.class);

        if (list1 == null || list1.isEmpty()) return list2 != null ? new ArrayList<>(list2) : new ArrayList<>();

        if (list2 == null || list2.isEmpty()) return new ArrayList<>(list1);

        if (list1 == list2) return new ArrayList<>(list1); // Tránh trường hợp tham chiếu đến cùng một danh sách

        for (StatModifier mod : list1) {
            merged.put(mod.getStat(), mod.getValue());
        }

        for (StatModifier mod : list2) {
            merged.merge(mod.getStat(), mod.getValue(), Double::sum);
        }

        List<StatModifier> result = new ArrayList<>();
        for (Map.Entry<StatType, Double> entry : merged.entrySet()) {
            result.add(new StatModifier(entry.getKey(), entry.getValue(), "total"));
        }

        return result;
    }

}

