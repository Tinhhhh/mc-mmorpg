package net.tinhvv.inventoryProvider.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.tinhvv.equip.EquipmentType;
import net.tinhvv.equip.PlayerEquipment;
import net.tinhvv.items.misc.EmptySlotItem;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EquipmentProvider implements InventoryProvider {

    private final static Logger LOGGER = Bukkit.getLogger();
    private final FileConfiguration config;

    public EquipmentProvider(String fileName) {
        File file = new File(JavaPlugin.getPlugin(Mmorpg.class).getDataFolder(), "gui/" + fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    private static final Map<EquipmentType, Integer> slotMapping = Map.of(
            EquipmentType.AMULET, 0,
            EquipmentType.RING, 1,
            EquipmentType.GLOVES, 2,
            EquipmentType.BELT, 3
    );

    @Override
    public void init(Player player, InventoryContents contents) {

        int rows = config.getInt("size", 1);
        handleFill(contents, rows);
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items == null) return;


        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) continue;

            // Lấy vị trí (row, col) từ "pos"
            String[] pos = item.getString("pos", "0,0").split(",");
            int row = Integer.parseInt(pos[0]);
            int col = Integer.parseInt(pos[1]);

            // Lấy material từ YAML (không toUpperCase!)
            String matName = item.getString("material", "stone").toUpperCase();
            Material material = Material.matchMaterial(matName);
            if (material == null) {
                Bukkit.getLogger().warning("[EquipmentGUI] Invalid material for key '" + key + "': " + matName);
                material = Material.STONE;
            }

            // Lấy item nếu có trang bị, nếu null thì tạo item trống theo material YAML
            ItemStack stack;
            switch (key.toLowerCase()) {
                case "amulet" -> stack = GetEquipment(EquipmentType.AMULET, player);
                case "ring" -> stack = GetEquipment(EquipmentType.RING, player);
                case "gloves" -> stack = GetEquipment(EquipmentType.GLOVES, player);
                case "belt" -> stack = GetEquipment(EquipmentType.BELT, player);
                default -> stack = null;
            }

            if (stack == null || stack.getType().isAir()) {
                stack = createNormalItem(player, item, material); // fallback nếu không có item
            }

            EquipmentType type = switch (key.toLowerCase()) {
                case "amulet" -> EquipmentType.AMULET;
                case "ring" -> EquipmentType.RING;
                case "gloves" -> EquipmentType.GLOVES;
                case "belt" -> EquipmentType.BELT;
                default -> null;
            };

            if (type == null) continue;

            final EquipmentType finalType = type;

            contents.set(SlotPos.of(row, col), ClickableItem.of(stack, event -> {
                ItemStack cursor = event.getCursor();
                Player p = (Player) event.getWhoClicked();

                // Nếu tay đang cầm item
                if (cursor != null && cursor.getType() != Material.AIR) {
                    // Check tag: ví dụ tag name = amulet
                    //finalType: Truyền vào items key của slot (amulet, ring, gloves, belt)
                    if (!isValidEquipmentItem(cursor, finalType)) {
                        p.sendMessage(ChatColor.RED + "Bạn không thể trang bị item này vào ô " + finalType.name().toLowerCase());
                        return;
                    }

                    // Lưu vào equipment manager
                    Mmorpg.getEquipmentManager().get(p).setItem(finalType, cursor.clone());
                    Mmorpg.getEquipmentManager().save(p);
                    Mmorpg.getStatManager().updateFromAllEquipment(p);
                    event.setCursor(null);

                    // 🟩 Reload GUI để hiển thị thay đổi
                    EquipmentProvider.open(p);
                }
                // Nếu tay không cầm gì => gỡ item khỏi equipment
                else {

                    // Gỡ item cũ
                    PlayerEquipment equipment = Mmorpg.getEquipmentManager().getOrCreate(player);
                    ItemStack old = equipment.getItem(finalType);

                    if (old != null && old.getType() != Material.AIR) {
                        // Ưu tiên đưa vào tay nếu tay đang trống
                        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            p.getInventory().setItemInMainHand(old.clone());
                        } else {
                            // Nếu tay bận → cho vào inventory
                            HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(old.clone());
                            if (!overflow.isEmpty()) {
                                // Nếu full inventory → drop ra đất
                                for (ItemStack leftover : overflow.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), leftover);
                                }
                            }
                        }
                    }

                    Mmorpg.getEquipmentManager().get(p).setItem(finalType, null);
                    // 🔁 Lưu sau khi gỡ trang bị
                    Mmorpg.getEquipmentManager().save(p);
                    Mmorpg.getStatManager().updateFromAllEquipment(p);
                    EquipmentProvider.open(p);
                }
            }));
        }
    }

    private boolean isValidEquipmentItem(ItemStack item, EquipmentType type) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(Mmorpg.getInstance(), type.name());
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            LOGGER.info("Item display name: " + item.getItemMeta().getDisplayName() + ", key: " + key);
            String itemTag = key.toString().split(":")[1];
            LOGGER.info("Item tag found: " + itemTag);
            LOGGER.info("Slot tag found: " + type.name());
            return itemTag.equalsIgnoreCase(type.name().toLowerCase());
        }
        return false;
    }


    private ItemStack GetEquipment(EquipmentType equipmentType, Player player) {
        PlayerEquipment equipment = Mmorpg.getEquipmentManager().getOrCreate(player);
        if (equipment == null) return null;

        ItemStack item = equipment.getItem(equipmentType);
        if (item == null || item.getType() == Material.AIR) {
            return null; // <-- thay vì return new MissingItem().create();
        }
        return item;
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

    @Override
    public void update(Player player, InventoryContents contents) {
        // optional update
    }

    private ItemStack createNormalItem(Player player, ConfigurationSection item, Material material) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        String rawName = item.getString("display_name", "");
        String displayName = ChatColor.translateAlternateColorCodes('<',
                StatFormat.processStatPlaceholder(rawName.replace("{player}", player.getName()), player));
        meta.setDisplayName(displayName);

        if (item.contains("lore")) {
            List<String> rawLore = item.getStringList("lore");
            List<String> lore = rawLore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('<',
                            StatFormat.processStatPlaceholder(line.replace("{player}", player.getName()), player)))
                    .collect(Collectors.toList());
            meta.setLore(lore);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    public static void open(Player player) {
        SmartInventory.builder()
                .id("equipment")
                .provider(new EquipmentProvider("equipment.yml"))
                .size(1, 9)
                .title("Your equipment")
                .manager(Mmorpg.getInventoryManager())
                .build().open(player);
    }
}
