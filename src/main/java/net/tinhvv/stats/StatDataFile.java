package net.tinhvv.stats;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class StatDataFile {

    private final File file;
    private final YamlConfiguration config;

    public StatDataFile(UUID uuid, File dataFolder) {
        File folder = new File(dataFolder, "data");
        if (!folder.exists()) folder.mkdirs();

        this.file = new File(folder, uuid.toString() + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Map<StatType, Double> loadStats() {
        Map<StatType, Double> stats = new EnumMap<>(StatType.class);

        for (StatType type : StatType.values()) {
            double value = config.getDouble("equipment." + type.name(), 0.0);
            stats.put(type, value);
        }

        return stats;
    }

    public void saveStats(Map<StatType, Double> stats) {
        for (Map.Entry<StatType, Double> entry : stats.entrySet()) {
            config.set("stats." + entry.getKey().name(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
