package net.tinhvv.manager;

import net.tinhvv.equip.EquipmentType;
import net.tinhvv.equip.PlayerEquipment;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItem;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatManager {

    private static final Logger LOGGER = Logger.getLogger(StatManager.class.getName());

    // Player stat data (base level of each stat)
    private final Map<UUID, PlayerBaseStats> data = new ConcurrentHashMap<>();

    // Active modifiers (from items, buffs, effects...)
    private final Map<UUID, List<StatModifier>> modifierMap = new ConcurrentHashMap<>();

    /**
     * Get or create base stat data for a player
     */
    public PlayerBaseStats get(Player player) {
        return data.computeIfAbsent(player.getUniqueId(), k -> new PlayerBaseStats());
    }

    public void setModifiers(UUID uuid, List<StatModifier> newModifiers) {
        modifierMap.put(uuid, newModifiers);
    }


    /**
     * Get total stat: base + equipment + extra modifiers
     */
    public double getTotalFromOneStat(Player player, StatType stat) {
        List<StatModifier> modifiers = calculatePlayerStats(player); // ← dùng lại luôn
        return modifiers.stream()
                .filter(mod -> mod.getStat() == stat)
                .mapToDouble(StatModifier::getValue)
                .sum();
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


    public List<StatModifier> calculatePlayerStats(Player player) {
        List<StatModifier> playerModifiers = get(player).getBase();

//        Đọc chỉ số từ equipped armor
        PlayerInventory inv = player.getInventory();
        List<StatModifier> helmetModifiers = new ArrayList<>();
        ItemStack helmet = inv.getHelmet();
        if (helmet != null && helmet.getType() != Material.AIR) {
            helmetModifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(helmet);
//            LOGGER.info("[StatManager] Applying helmet stats: " + helmetModifiers);
        }

        List<StatModifier> chestplateModifiers = new ArrayList<>();
        ItemStack chestplate = inv.getChestplate();
        if (chestplate != null && chestplate.getType() != Material.AIR) {
            chestplateModifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(chestplate);
//            LOGGER.info("[StatManager] Applying chestplate stats: " + chestplateModifiers);
        }

        List<StatModifier> leggingsModifiers = new ArrayList<>();
        ItemStack leggings = inv.getLeggings();
        if (leggings != null && leggings.getType() != Material.AIR) {
            leggingsModifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(leggings);
//            LOGGER.info("[StatManager] Applying leggings stats: " + leggingsModifiers);
        }

        List<StatModifier> bootsModifiers = new ArrayList<>();
        ItemStack boots = inv.getBoots();
        if (boots != null && boots.getType() != Material.AIR) {
            bootsModifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(boots);
//            LOGGER.info("[StatManager] Applying boots stats: " + StatModifier.getValueByStatType(bootsModifiers, StatType.SPEED));
        }

        // Đọc chỉ số từ equipment
        PlayerEquipment equip = Mmorpg.getEquipmentInvManager().get(player);
        Map<EquipmentType, ItemStack> equipMap = equip.getAllEquipment();

        //Lấy chỉ số từ equip

        Map<EquipmentType, List<StatModifier>> equipModifiers = new EnumMap<>(EquipmentType.class);

        for (EquipmentType type : equipMap.keySet()) {
            ItemStack equipment = equipMap.get(type);
            List<StatModifier> modifiers = new ArrayList<>();
            if (equipment != null && equipment.getType() != Material.AIR) {
//                LOGGER.info("[StatManager] Item " + type.name().toLowerCase() + " stats: " + equipment.getType());
                modifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(equipment);
            }

            if (!modifiers.isEmpty()) {
                equipModifiers.put(type, modifiers);
            }
        }

        //Đọc chỉ số từ các nguồn khác (future plans...)

        //Đọc chỉ số từ tay chính
        List<StatModifier> mainHandModifiers = new ArrayList<>();
        ItemStack mainHand = inv.getItemInMainHand();
        if (mainHand.getType() != Material.AIR) {
            if (isWeapon(mainHand)) {
                mainHandModifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(mainHand);
                LOGGER.info("[StatManager] Applying atk speed stats: " + StatModifier.getValueByStatType(mainHandModifiers, StatType.ATTACK_SPEED));
                LOGGER.info("[StatManager] Applying base damage stats: " + StatModifier.getValueByStatType(mainHandModifiers, StatType.BASE_DAMAGE));
                LOGGER.info("[StatManager] Applying strength stats: " + StatModifier.getValueByStatType(mainHandModifiers, StatType.STRENGTH));
            } else {
                LOGGER.warning("[StatManager] Item in main hand is not a weapon: " + mainHand.getType());
            }
        }

        // Combine all modifiers
        List<StatModifier> allModifiers;
        StatItemParser statItemParser = new StatItemParser();
        allModifiers = statItemParser.merge(playerModifiers, helmetModifiers);
        allModifiers = statItemParser.merge(allModifiers, chestplateModifiers);
        allModifiers = statItemParser.merge(allModifiers, leggingsModifiers);
        allModifiers = statItemParser.merge(allModifiers, bootsModifiers);
        allModifiers = statItemParser.merge(allModifiers, mainHandModifiers);

        for (Map.Entry<EquipmentType, List<StatModifier>> entry : equipModifiers.entrySet()) {
            allModifiers = statItemParser.merge(allModifiers, entry.getValue());
        }

//         Apply to real gameplay
        clearModifiers(player);
        setModifiers(player.getUniqueId(), allModifiers);
        StatApplier.applyAllStats(player);
//        LOGGER.info("[StatManager - END] Calculating stats for player: =============" + player.getName() + "===============");
        return allModifiers;
    }

    public List<StatModifier> getEquipmentModifiers(Player player) {
        List<StatModifier> modifiers = new ArrayList<>();

        PlayerEquipment equipment = Mmorpg.getEquipmentInvManager().get(player);
        if (equipment == null) return modifiers;

        for (EquipmentType type : EquipmentType.values()) {
            ItemStack item = equipment.getItem(type);
            if (item == null || item.getType().isAir()) continue;

            // Kiểm tra item có phải là custom item không
            Optional<CustomItem> custom = Mmorpg.getCustomItemManager().match(item);
            if (custom.isPresent() && custom.get() instanceof AbstractCustomItem customItem) {
                modifiers.addAll(customItem.getStatModifiers());
            }
        }

        return modifiers;
    }

//    public void updateMainHandModifiers(Player player, ItemStack item) {
//        if (item == null || item.getType().isAir()) return;
//
//        if (!isWeapon(item)) {
//            LOGGER.warning("[StatManager] Item in main hand is not a weapon: " + item.getType());
//            return;
//        }
//
//        List<StatModifier> modifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(item);
//
//        LOGGER.info("[StatManager] Added modifier from main hand item: " + StatModifier.getValueByStatType(modifiers, StatType.STRENGTH));
//
//        StatItemParser statItemParser = new StatItemParser();
//        List<StatModifier> globalStats = statItemParser.merge(modifiers, Mmorpg.getStatManager().calculatePlayerStats(player));
//        setModifiers(player.getUniqueId(), globalStats);
//        StatApplier.apply(player);
//    }

    public void removeModifiersBySource(Player player, String source) {
        UUID uuid = player.getUniqueId();
        List<StatModifier> modifiers = modifierMap.getOrDefault(uuid, new ArrayList<>());

        // Lọc những modifier KHÔNG phải từ source cần xóa
        List<StatModifier> remaining = modifiers.stream()
                .filter(mod -> !mod.getSource().equalsIgnoreCase(source))
                .collect(Collectors.toList());

        modifierMap.put(uuid, remaining);
    }

    public boolean isWeapon(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            LOGGER.warning("[StatManager] Item is null or air, cannot check if it's a weapon.");
            return false;
        }

        // Kiểm tra xem item có phải là vũ khí không
        String itemSource = StatItemParser.getItemSource(item);
        if (itemSource.startsWith("VANILLA:")) {
            if (itemSource.contains("_SWORD") || itemSource.contains("_AXE") || itemSource.contains("BOW")
                    || itemSource.contains("TRIDENT") || itemSource.contains("MACE") || itemSource.contains("CROSSBOW")) {
                LOGGER.info("[StatManager] Item " + item.getType() + " is recognized as a weapon: " + itemSource);
            } else {
                LOGGER.warning("[StatManager] Item " + item.getType() + " is not recognized as a weapon: " + itemSource);
            }
            return true;
        }

        if (itemSource.startsWith("CUSTOM:WEAPON_")) {
            LOGGER.info("[StatManager] Item " + item.getType() + " is recognized as a custom weapon: " + itemSource);
            return true;
        }
        LOGGER.warning("[StatManager] Item " + item.getType() + " type is not recognized as a weapon: " + itemSource);
        return false;
    }


}
