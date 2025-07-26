package net.tinhvv.manager;

import net.tinhvv.equip.EquipmentType;
import net.tinhvv.equip.PlayerEquipment;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItem;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StatManager {

    // Player stat data (base level of each stat)
    private final Map<UUID, PlayerStatData> data = new ConcurrentHashMap<>();

    // Active modifiers (from items, buffs, effects...)
    private final Map<UUID, List<StatModifier>> modifierMap = new ConcurrentHashMap<>();

    /**
     * Get or create base stat data for a player
     */
    public PlayerStatData get(Player player) {
        return data.computeIfAbsent(player.getUniqueId(), k -> new PlayerStatData());
    }

    /**
     * Load player stat data (from file/db - to be implemented)
     */
    public void load(Player player) {
        StatDataFile dataFile = new StatDataFile(player.getUniqueId(), Mmorpg.getInstance().getDataFolder());
        PlayerStatData statData = new PlayerStatData();

        Map<StatType, Double> loaded = dataFile.loadStats();
        for (Map.Entry<StatType, Double> entry : loaded.entrySet()) {
            statData.setStat(entry.getKey(), entry.getValue());
        }

        data.put(player.getUniqueId(), statData);
    }

    /**
     * Unload player data (cleanup)
     */
    public void unload(Player player) {
        PlayerStatData statData = data.remove(player.getUniqueId());
        if (statData != null) {
            StatDataFile dataFile = new StatDataFile(player.getUniqueId(), Mmorpg.getInstance().getDataFolder());
            dataFile.saveStats(statData.getAll());
        }
        modifierMap.remove(player.getUniqueId());
    }

    public void save(Player player) {
        PlayerStatData statData = data.get(player.getUniqueId());
        if (statData != null) {
            System.out.println("[StatManager] Saving stats: " + statData.getAll());
            StatDataFile dataFile = new StatDataFile(player.getUniqueId(), Mmorpg.getInstance().getDataFolder());
            dataFile.saveStats(statData.getAll());
        }
    }


    /**
     * Get total stat: base + equipment + extra modifiers
     */
    public double getTotalStat(Player player, StatType stat) {
        double total = get(player).getStat(stat);

        // Modifiers từ item trang bị
        PlayerEquipment equip = Mmorpg.getEquipmentManager().get(player);
        for (ItemStack item : equip.getAllEquipped()) {
            Optional<CustomItem> custom = CustomItemManager.match(item);
            if (custom.isPresent() && custom.get() instanceof AbstractCustomItem aci) {
                for (StatModifier mod : aci.getStatModifiers()) {
                    if (mod.getStat() == stat) {
                        total += mod.getValue();
                    }
                }
            }
        }

        // Modifiers tạm thời (buff, skill, v.v.)
        List<StatModifier> otherModifiers = modifierMap.getOrDefault(player.getUniqueId(), Collections.emptyList());
        for (StatModifier mod : otherModifiers) {
            if (mod.getStat() == stat) {
                total += mod.getValue();
            }
        }

        return total;
    }


    /**
     * Add a stat modifier for a player
     */
    public void addModifier(Player player, StatModifier modifier) {
        modifierMap.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(modifier);
    }

    /**
     * Remove a specific modifier by source ID
     */
    public void removeModifierBySource(Player player, String source) {
        List<StatModifier> list = modifierMap.get(player.getUniqueId());
        if (list != null) {
            list.removeIf(mod -> mod.getSource().equalsIgnoreCase(source));
        }
    }

    /**
     * Clear all modifiers for a player (e.g. on logout or item unequip)
     */
    public void clearModifiers(Player player) {
        modifierMap.remove(player.getUniqueId());
    }

    /**
     * Get all modifiers for debug or display
     */
    public List<StatModifier> getModifiers(Player player) {
        return modifierMap.getOrDefault(player.getUniqueId(), Collections.emptyList());
    }


    public void updateFromAllEquipment(Player player) {
        // Remove old modifiers
        for (String slot : List.of("HEAD", "CHEST", "LEGS", "FEET")) {
            removeModifierBySource(player, "equip_" + slot);
        }

        // Apply modifiers from currently equipped armor
        PlayerInventory inv = player.getInventory();
        applyItemModifier(player, inv.getHelmet(), "equip_HEAD");
        applyItemModifier(player, inv.getChestplate(), "equip_CHEST");
        applyItemModifier(player, inv.getLeggings(), "equip_LEGS");
        applyItemModifier(player, inv.getBoots(), "equip_FEET");

        // Apply to real gameplay
        StatApplier.apply(player, this);
    }

    private void applyItemModifier(Player player, ItemStack item, String source) {
        if (item == null || item.getType().isAir()) return;

        List<StatModifier> modifiers = StatItemParser.parse(item, source);

        // Nếu lore không có stat → fallback dùng stat mặc định của item vanilla
        if (modifiers.isEmpty()) {
            modifiers = VanillaArmorStats.getStats(item, source);
        }

        for (StatModifier mod : modifiers) {
            addModifier(player, mod);
        }
    }

    public List<StatModifier> getEquipmentModifiers(Player player) {
        List<StatModifier> modifiers = new ArrayList<>();

        PlayerEquipment equipment = Mmorpg.getEquipmentManager().get(player);
        if (equipment == null) return modifiers;

        for (EquipmentType type : EquipmentType.values()) {
            ItemStack item = equipment.getItem(type);
            if (item == null || item.getType().isAir()) continue;

            // Kiểm tra item có phải là custom item không
            Optional<CustomItem> custom = CustomItemManager.match(item);
            if (custom.isPresent() && custom.get() instanceof AbstractCustomItem customItem) {
                modifiers.addAll(customItem.getStatModifiers());
            }
        }

        return modifiers;
    }


}
