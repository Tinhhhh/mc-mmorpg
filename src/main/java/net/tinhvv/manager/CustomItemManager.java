package net.tinhvv.manager;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItem;
import net.tinhvv.items.easterEgg.GoblinEgg;
import net.tinhvv.items.equipment.accessory.amulet.Seashell;
import net.tinhvv.items.equipment.accessory.gloves.InfinityGauntlet;
import net.tinhvv.items.equipment.accessory.ring.OneRing;
import net.tinhvv.items.equipment.armor.boots.ConverseC70;
import net.tinhvv.items.misc.EmptySlotItem;
import net.tinhvv.items.misc.MenuItem;
import net.tinhvv.items.misc.MissingItem;
import net.tinhvv.items.weapon.BluntSword;
import net.tinhvv.items.weapon.RemoveEntitySword;
import net.tinhvv.items.weapon.SilverDagger;
import net.tinhvv.items.weapon.WeirdStick;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatItemParser;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import net.tinhvv.stats.VanillaItemStats;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class CustomItemManager {

    private static final Map<String, CustomItem> items = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(CustomItemManager.class.getName());


    /*
    Lấy ra CustomItem tương ứng từ ItemStack
     */
    public Optional<CustomItem> match(ItemStack item) {
        return items.values().stream().filter(ci -> ci.isMatch(item)).findFirst();
    }

    /**
     * Lấy CustomItem theo ID
     *
     * @param id ID của CustomItem cần lấy
     * @return CustomItem nếu tìm thấy, null nếu không tìm thấy
     */
    public CustomItem getById(String id) {
        return items.get(id);
    }

    public Collection<CustomItem> getAll() {
        return items.values();
    }

    public String getTagName(ItemStack item) {
        // Kiểm tra xem item có phải là CustomItem không
        Optional<CustomItem> match = match(item);
        if (match.isPresent()) {
            return match.get().getTagName(); // Trả về ID của CustomItem
        }
        return "UNKNOWN"; // Không xác định được loại item
    }

    public String getId(ItemStack item) {
        // Kiểm tra xem item có phải là CustomItem không
        Optional<CustomItem> match = match(item);
        if (match.isPresent()) {
            return match.get().getId(); // Trả về ID của CustomItem
        }
        return "UNKNOWN"; // Không xác định được loại item
    }

    public List<StatModifier> getModifiersByItemStack(ItemStack item) {

        //Mặc định không có modifier nào
        List<StatModifier> modifiers = new ArrayList<>();

        String itemSource = StatItemParser.getItemSource(item);
        if (itemSource.startsWith("VANILLA:")) {
            // Nếu không phải CustomItem thì kiểm tra xem có phải item vanilla không
            modifiers = VanillaItemStats.getStats(item, itemSource);
        }

        // Nếu là CustomItem thì lấy các modifier từ item đó
        if (itemSource.startsWith("CUSTOM:")) {
            Optional<CustomItem> custom = Mmorpg.getCustomItemManager().match(item);
            if (custom.isPresent() && custom.get() instanceof AbstractCustomItem aci) {
                modifiers = aci.getStatModifiers();
            }
        }

        LOGGER.info("modifiers: " + modifiers);
        return modifiers;
    }


    // Gọi hàm này trong onEnable()
    public void init() {
        List<CustomItem> items = new ArrayList<>(List.of(
                new GoblinEgg(),
                new MenuItem(),
                new EmptySlotItem(),
                new MissingItem(),
                new Seashell(),
                new RemoveEntitySword(),
                new BluntSword(),
                new ConverseC70(),
                new OneRing(),
                new WeirdStick(),
                new InfinityGauntlet(),
                new SilverDagger()
        ));


        File folder = new File(Mmorpg.getInstance().getDataFolder(), "items");
        if (!folder.exists()) folder.mkdirs();

        for (CustomItem item : items) {
            if (item instanceof AbstractCustomItem abstractItem) {
                File file = new File(folder, abstractItem.getId() + ".yml");
                register(abstractItem, file);
                abstractItem.loadFromFile(file);
            }
        }
    }

    public void register(CustomItem item, File sourceFile) {
        items.put(item.getId(), item);
        if (!(item instanceof AbstractCustomItem aci)) return;

        File itemFile = new File(Mmorpg.getInstance().getDataFolder(), "items/" + aci.getId() + ".yml");

        try {
            aci.saveToFile(itemFile);
//            aci.loadFromFile(itemFile);
            LOGGER.info("[CustomItemManager] Overridden item config for: " + aci.getId());
        } catch (Exception e) {
            LOGGER.severe("[CustomItemManager] Failed to load/save item: " + aci.getId());
            logItemError(aci.getId(), sourceFile.getName(), e);
        }
    }

    private static void logItemError(String itemId, String sourceFile, Exception e) {
        File logFile = new File(Mmorpg.getInstance().getDataFolder() + "/log/items", "log_item_" + itemId + ".yml");
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

    public void reloadItemMetaIfCustom(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;

        // Kiểm tra xem item có phải là CustomItem không
        ItemMeta meta = item.getItemMeta();
        String itemId = Mmorpg.getCustomItemManager().getId(item);
        Bukkit.getLogger().info("[CustomItemManager] Reloading item meta for: " + itemId);
        if (itemId == null) return;

        CustomItem customItem = Mmorpg.getCustomItemManager().getById(itemId);
        if (!(customItem instanceof AbstractCustomItem aci)) return;

        // Lấy lại thông tin từ file
        ItemStack itemStack = aci.findItemInFile(aci.getId());
        if (itemStack == null || !itemStack.hasItemMeta()) return;
        ItemMeta itemData = itemStack.getItemMeta();

        // Ghi đè displayName + lore
        meta.setDisplayName(itemData.getDisplayName());
        meta.setLore(itemData.getLore());
        Bukkit.getLogger().info("[CustomItemManager] Found item displayName: " + itemData.getDisplayName() + " for: " + aci.getId());
        Bukkit.getLogger().info("[CustomItemManager] Found item lore " + itemData.getLore() + " for: " + aci.getId());


        // Ghi đè baseStats vào PDC
        for (Map.Entry<StatType, Double> entry : aci.getBaseStats().entrySet()) {
            NamespacedKey statKey = new NamespacedKey(Mmorpg.getInstance(), entry.getKey().name());
            meta.getPersistentDataContainer().set(statKey, PersistentDataType.DOUBLE, entry.getValue());
        }
        item.setItemMeta(meta);
        Bukkit.getLogger().info("[CustomItemManager] Reloaded success item meta for: " + aci.getId());
    }


}
