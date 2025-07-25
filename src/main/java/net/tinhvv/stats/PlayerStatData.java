package net.tinhvv.stats;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class PlayerStatData {

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
