package net.tinhvv.stats;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class PlayerStatData {

    public PlayerStatData() {
        // Gán giá trị base cho các chỉ số chính
        setStat(StatType.STRENGTH, 0.0);
        setStat(StatType.HEALTH, 20.0);
        setStat(StatType.REGENERATION, 0.0);
        setStat(StatType.ARMOR, 0.0);
        setStat(StatType.TOUGHNESS, 0.0);
        setStat(StatType.WISDOM, 20.0);
        setStat(StatType.SPEED, 1.0);
        setStat(StatType.LUCK, 0.0);
        setStat(StatType.CRIT_CHANCE, 20.0);
        setStat(StatType.CRIT_DAMAGE, 100.0);
    }

    private final EnumMap<StatType, Double> stats = new EnumMap<>(StatType.class);

    public double getStat(StatType stat) {
        return stats.getOrDefault(stat, 0.0);
    }

    public void setStat(StatType stat, double value) {
        stats.put(stat, value);
    }

    public void addStat(StatType stat, double amount) {
        stats.put(stat, getStat(stat) + amount);
    }

    public Map<StatType, Double> getAll() {
        return Collections.unmodifiableMap(stats);
    }
}
