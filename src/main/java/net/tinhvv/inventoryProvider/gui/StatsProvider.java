package net.tinhvv.inventoryProvider.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.tinhvv.equip.EquipmentGUI;
import net.tinhvv.equip.EquipmentManager;
import net.tinhvv.equip.EquipmentType;
import net.tinhvv.equip.PlayerEquipment;
import net.tinhvv.items.misc.EmptySlotItem;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
        int rows = config.getInt("size", 6);
        handleFill(contents, rows);

        ConfigurationSection items = config.getConfigurationSection("items");
        if (items == null) return;

        PlayerInventory inv = player.getInventory();
        PlayerEquipment equipment = EquipmentManager.get(player);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) continue;

            String[] pos = item.getString("pos", "0,0").split(",");
            int row = Integer.parseInt(pos[0]);
            int col = Integer.parseInt(pos[1]);

            Material material = Material.matchMaterial(item.getString("material", "STONE"));
            if (material == null) material = Material.STONE;

            ItemStack stack;
            switch (key.toLowerCase()) {
                case "helmet" -> stack = inv.getHelmet();
                case "chestplate" -> stack = inv.getChestplate();
                case "leggings" -> stack = inv.getLeggings();
                case "boots" -> stack = inv.getBoots();
                case "amulet", "ring", "gloves", "belt" -> stack = equipment.getItem(parseEquipmentType(key));
                case "skull" -> stack = createStatSkull(player, item);
                default -> stack = createNormalItem(player, item, material);
            }

            if (stack == null || stack.getType().isAir()) {
                stack = createNormalItem(player, item, material);
            }

            // Click để tháo / lắp đồ
            contents.set(SlotPos.of(row, col), ClickableItem.empty(stack));
//            contents.set(SlotPos.of(row, col), ClickableItem.of(displayItem, e -> {
//                ItemStack cursor = player.getItemOnCursor();
//                boolean isCursorEmpty = (cursor == null || cursor.getType().isAir());
//
//                switch (key.toLowerCase()) {
//                    case "helmet" -> {
//                        if (isCursorEmpty) {
//                            player.setItemOnCursor(inv.getHelmet());
//                            inv.setHelmet(null);
//                        } else {
//                            inv.setHelmet(cursor.clone());
//                            player.setItemOnCursor(null);
//                        }
//                    }
//                    case "chestplate" -> {
//                        if (isCursorEmpty) {
//                            player.setItemOnCursor(inv.getChestplate());
//                            inv.setChestplate(null);
//                        } else {
//                            inv.setChestplate(cursor.clone());
//                            player.setItemOnCursor(null);
//                        }
//                    }
//                    case "leggings" -> {
//                        if (isCursorEmpty) {
//                            player.setItemOnCursor(inv.getLeggings());
//                            inv.setLeggings(null);
//                        } else {
//                            inv.setLeggings(cursor.clone());
//                            player.setItemOnCursor(null);
//                        }
//                    }
//                    case "boots" -> {
//                        if (isCursorEmpty) {
//                            player.setItemOnCursor(inv.getBoots());
//                            inv.setBoots(null);
//                        } else {
//                            inv.setBoots(cursor.clone());
//                            player.setItemOnCursor(null);
//                        }
//                    }
//                    case "amulet", "ring", "gloves", "belt" -> {
//                        EquipmentType type = parseEquipmentType(key);
//                        if (isCursorEmpty) {
//                            player.setItemOnCursor(equipment.getItem(type));
//                            equipment.setItem(type, null);
//                        } else {
//                            equipment.setItem(type, cursor.clone());
//                            player.setItemOnCursor(null);
//                        }
//                    }
//                    default -> {
//                        // không thao tác
//                    }
//                }
//
//                // reload GUI
//            }));
        }
    }

    private EquipmentType parseEquipmentType(String key) {
        return switch (key.toLowerCase()) {
            case "amulet" -> EquipmentType.AMULET;
            case "ring" -> EquipmentType.RING;
            case "gloves" -> EquipmentType.GLOVES;
            case "belt" -> EquipmentType.BELT;
            default -> throw new IllegalArgumentException("Unknown slot: " + key);
        };
    }

    private void handleFill(InventoryContents contents, int rows) {
        ConfigurationSection fillSection = config.getConfigurationSection("fill");
        if (fillSection == null || !fillSection.getBoolean("enabled", false)) return;

        ItemStack filler = new EmptySlotItem().create();
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName("");
        meta.setLore(Collections.emptyList());
        for (ItemFlag flag : ItemFlag.values()) meta.addItemFlags(flag);
        filler.setItemMeta(meta);

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
        // optional update
    }

    public static void open(Player player) {
        SmartInventory.builder()
                .id("stats")
                .provider(new StatsProvider("stats.yml"))
                .size(6, 9)
                .title(EquipmentGUI.GUI_TITLE)
                .manager(Mmorpg.getInventoryManager())
                .build()
                .open(player);
    }
}
