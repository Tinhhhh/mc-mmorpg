package net.tinhvv.stats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class StatDataFile {

    private final File file;
    private final YamlConfiguration config;
    private final UUID playerId;

    public StatDataFile(UUID uuid, File dataFolder) {
        this.playerId = uuid;

        File folder = new File(dataFolder, "data");
        if (!folder.exists() && folder.mkdirs()) {
            Bukkit.getLogger().info("[StatDataFile] Created data folder: " + folder.getAbsolutePath());
        }

        this.file = new File(folder, uuid.toString() + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Map<StatType, Double> loadStats() {
        Map<StatType, Double> stats = new EnumMap<>(StatType.class);

        for (StatType type : StatType.values()) {
            double value = config.getDouble("stats." + type.name(), 0.0);
            stats.put(type, value);
        }

        System.out.println("[StatDataFile] Loaded stats for " + file.getName() + ": " + stats);
        return stats;
    }


    public void saveStats(Map<StatType, Double> stats) {
        for (Map.Entry<StatType, Double> entry : stats.entrySet()) {
            config.set("stats." + entry.getKey().name(), entry.getValue());
        }
        try {
            config.save(file);
            Bukkit.getLogger().info("[StatDataFile] Saved stats for " + playerId + ": " + stats);
        } catch (IOException e) {
            Bukkit.getLogger().severe("[StatDataFile] Failed to save stats for " + playerId);
            e.printStackTrace();
        }
    }
}
