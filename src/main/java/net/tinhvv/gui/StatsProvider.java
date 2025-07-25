package net.tinhvv.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatsProvider implements InventoryProvider {

    private final FileConfiguration config;

    public StatsProvider(String fileName) {
        File file = new File(JavaPlugin.getPlugin(Mmorpg.class).getDataFolder(), "gui/" + fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        // 1. Lấy số dòng của GUI
        int rows = config.getInt("size", 6);

        // 2. Xử lý background filler nếu được bật trong YAML
        handleFill(contents, rows);

        // 3. Duyệt qua các item trong YAML và hiển thị lên GUI
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items == null) return;

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) continue;

            // Lấy vị trí (row, col) từ "pos"
            String[] pos = item.getString("pos", "0,0").split(",");
            int row = Integer.parseInt(pos[0]);
            int col = Integer.parseInt(pos[1]);

            // Lấy material từ config
            Material material = Material.matchMaterial(item.getString("material", "STONE"));
            if (material == null) continue;

            // Xử lý riêng nếu là skull
            ItemStack stack = "skull".equalsIgnoreCase(key)
                    ? createStatSkull(player, item)
                    : createNormalItem(player, item, material);

            contents.set(SlotPos.of(row, col), ClickableItem.empty(stack));
        }
    }

    private void handleFill(InventoryContents contents, int rows) {
        ConfigurationSection fillSection = config.getConfigurationSection("fill");
        if (fillSection == null || !fillSection.getBoolean("enabled", false)) return;

        String materialName = fillSection.getString("material", "BLACK_STAINED_GLASS_PANE");
        Material fillMaterial = Material.matchMaterial(materialName.toUpperCase());
        if (fillMaterial == null) return;

        ItemStack filler = new ItemStack(fillMaterial);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                contents.set(SlotPos.of(row, col), ClickableItem.empty(filler));
            }
        }
    }

    private ItemStack createStatSkull(Player player, ConfigurationSection item) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();

        meta.setOwningPlayer(player);

        String rawName = item.getString("display_name", "").replace("{player}", player.getName());
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('<', rawName));

        List<String> lore = new ArrayList<>();
        for (StatType stat : StatType.values()) {
            double value = Mmorpg.getStatManager().getTotalStat(player, stat);
            lore.add(StatFormat.format(stat, value));
        }

        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack createNormalItem(Player player, ConfigurationSection item, Material material) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        String rawName = item.getString("display_name", "");
        String displayName = ChatColor.translateAlternateColorCodes('<',
                processStatPlaceholder(rawName.replace("{player}", player.getName()), player));
        meta.setDisplayName(displayName);

        if (item.contains("lore")) {
            List<String> rawLore = item.getStringList("lore");
            List<String> lore = rawLore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('<',
                            processStatPlaceholder(line.replace("{player}", player.getName()), player)))
                    .collect(Collectors.toList());
            meta.setLore(lore);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    private String processStatPlaceholder(String input, Player player) {
        Matcher matcher = Pattern.compile("\\{stat:([A-Z_]+)}").matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            try {
                StatType type = StatType.valueOf(key);
                double value = Mmorpg.getStatManager().getTotalStat(player, type);
                matcher.appendReplacement(sb, Matcher.quoteReplacement(StatFormat.format(type, value)));
            } catch (IllegalArgumentException e) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement("{invalid:" + key + "}"));
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }


    @Override
    public void update(Player player, InventoryContents contents) {
        // Optional: update logic
    }
}
