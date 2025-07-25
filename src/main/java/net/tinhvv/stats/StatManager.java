package net.tinhvv.stats;

import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;

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
        // TODO: Load from persistent storage
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

    /**
     * Get total stat: base + all modifiers
     */
    public double getTotalStat(Player player, StatType stat) {
        PlayerStatData base = get(player);
        double total = base.getStat(stat);

        List<StatModifier> modifiers = modifierMap.getOrDefault(player.getUniqueId(), Collections.emptyList());
        for (StatModifier mod : modifiers) {
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
}
