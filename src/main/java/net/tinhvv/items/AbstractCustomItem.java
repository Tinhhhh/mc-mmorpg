package net.tinhvv.items;

import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCustomItem implements CustomItem {

    private final String id;
    private NamespacedKey key;

    protected Material material;
    protected String displayName;
    protected List<String> lore;
    protected Map<StatType, Double> baseStats = new HashMap<>();

    protected AbstractCustomItem(String id, Material material, String tagName) {
        this.id = id;
        this.material = material;
        this.key = new NamespacedKey(Mmorpg.getInstance(), tagName);
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());
        meta.setLore(getLore());
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }

    protected String getDisplayName() {
        return displayName;
    }

    protected List<String> getLore() {
        return lore;
    }

    public Map<StatType, Double> getBaseStats() {
        return baseStats;
    }


    public String getId() {
        return id;
    }

    public String getTagName() {
        return key.getKey();
    }

    public boolean isMatch(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return id.equals(value);
    }

    public void onInteract(Player player, PlayerInteractEvent event) {
        // Mặc định không làm gì
    }

    public void onClick(Player player, InventoryClickEvent event) {
        // Mặc định không làm gì
    }

    private List<StatType> getOrderedStatTypes() {
        return List.of(
                StatType.STRENGTH, StatType.HEALTH, StatType.REGENERATION,
                StatType.ARMOR, StatType.TOUGHNESS, StatType.INTELLIGENT,
                StatType.LUCK, StatType.SPEED, StatType.CRIT_CHANCE, StatType.CRIT_DAMAGE
        );
    }

    /**
     * Trả về danh sách các StatModifier tương ứng với item này
     * Chỉ bao gồm các chỉ số có giá trị khác 0
     */
    public List<StatModifier> getStatModifiers() {
        List<StatModifier> list = new java.util.ArrayList<>();
        for (StatType type : getOrderedStatTypes()) {
            if (getBaseStats().containsKey(type)) {
                double value = getBaseStats().get(type);
                list.add(new StatModifier(type, value, getId()));
            }
        }
        return list;
    }


    public void saveToFile(File file) {
        YamlConfiguration config = new YamlConfiguration();

        config.set("id", id);
        config.set("material", material.name());
        config.set("tagName", key.getKey());

        config.set("display_name", displayName.replace(ChatColor.COLOR_CHAR, '&'));
        config.set("lore", lore.stream()
                .map(line -> line.replace(ChatColor.COLOR_CHAR, '&'))
                .collect(Collectors.toList()));

        ConfigurationSection stats = config.createSection("stats");
        for (Map.Entry<StatType, Double> entry : baseStats.entrySet()) {
            stats.set(entry.getKey().name(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            logItemError(getId(), file.getName(), e);
        }
    }


    private static void logItemError(String itemId, String sourceFile, Exception e) {
        File logFile = new File(Mmorpg.getInstance().getDataFolder(), "log_items_" + itemId + ".yml");
        YamlConfiguration log = new YamlConfiguration();
        log.set("id", itemId);
        log.set("source_file", sourceFile);
        log.set("error", e.toString());

        try {
            log.save(logFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadFromFile(File file) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            this.material = Material.valueOf(config.getString("material", material.name()));
            this.key = new NamespacedKey(Mmorpg.getInstance(), config.getString("tagName", getTagName()));
            this.displayName = ChatColor.translateAlternateColorCodes('&', config.getString("display_name", ""));
            this.lore = config.getStringList("lore").stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());

            ConfigurationSection statsSection = config.getConfigurationSection("stats");
            this.baseStats.clear();
            if (statsSection != null) {
                for (String key : statsSection.getKeys(false)) {
                    StatType type = StatType.valueOf(key);
                    double val = statsSection.getDouble(key);
                    this.baseStats.put(type, val);
                }
            }

        } catch (Exception e) {
            File logFolder = new File(Mmorpg.getInstance().getDataFolder(), "log/items");
            if (!logFolder.exists()) logFolder.mkdirs();

            File logFile = new File(logFolder, getId() + ".yml");
            YamlConfiguration log = new YamlConfiguration();
            log.set("id", getId());
            log.set("source_file", file.getName());
            log.set("error", e.toString());

            try {
                log.save(logFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Mmorpg.getInstance().getLogger().severe("[CustomItem] Failed to load file: " + file.getName());
            e.printStackTrace();
        }
    }

    public ItemStack findItemInFile(String id) {
        try {
            File file = new File(Mmorpg.getInstance().getDataFolder(), "items/" + id + ".yml");
            if (!file.exists()) {
                Bukkit.getLogger().severe("[CustomItem] File not found: " + file.getName());
                return null;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            // Parse material
            Material material = Material.matchMaterial(config.getString("material", "STONE"));
            if (material == null) material = Material.STONE;

            // Parse display name & lore
            String rawName = config.getString("display_name", "&fUnknown Item");
            String displayName = ChatColor.translateAlternateColorCodes('&', rawName);

            List<String> rawLore = config.getStringList("lore");
            List<String> lore = rawLore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());

            // Create item
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                meta.setLore(lore);
                NamespacedKey key = new NamespacedKey(Mmorpg.getInstance(), config.getString("tagName", "custom"));
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
                item.setItemMeta(meta);
            }

            return item;

        } catch (Exception e) {
            File logFile = new File(Mmorpg.getInstance().getDataFolder(), "log/items/log_item_" + id + ".yml");
            YamlConfiguration log = new YamlConfiguration();
            log.set("id", id);
            log.set("error", e.toString());
            try {
                log.save(logFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Bukkit.getLogger().severe("[CustomItem] Failed to load item from file: " + id);
            e.printStackTrace();
            return null;
        }
    }


}
