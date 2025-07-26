package net.tinhvv.manager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.tinhvv.equip.EquipmentType;
import net.tinhvv.equip.ItemSerializer;
import net.tinhvv.equip.PlayerEquipment;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EquipmentManager {

    private static final Map<UUID, PlayerEquipment> equipmentMap = new HashMap<>();

    public PlayerEquipment get(Player player) {
        return equipmentMap.get(player.getUniqueId());
    }

    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerEquipment data = equipmentMap.get(uuid);

        if (data == null) {
            Bukkit.getLogger().warning("[EquipmentManager] Tried to save equipment for " + player.getName() + " but data is null.");
            return;
        }

        File file = new File(Mmorpg.getInstance().getDataFolder(), "equipment/" + uuid + ".json");
        file.getParentFile().mkdirs();

        JsonObject json = new JsonObject();
        for (Map.Entry<EquipmentType, ItemStack> entry : data.getAllEquipment().entrySet()) {
            json.addProperty(entry.getKey().name(), ItemSerializer.toBase64(entry.getValue()));
        }

        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
            Bukkit.getLogger().info("[EquipmentManager] Saved equipment for " + player.getName() + " to " + file.getName());
        } catch (IOException e) {
            Bukkit.getLogger().severe("[EquipmentManager] Failed to save equipment for " + player.getName());
            e.printStackTrace();
        }
    }

    public PlayerEquipment load(Player player) {
        UUID uuid = player.getUniqueId();
        File file = new File(Mmorpg.getInstance().getDataFolder(), "equipment/" + uuid + ".json");
        PlayerEquipment data = new PlayerEquipment();

        if (!file.exists()) {
            Bukkit.getLogger().info("[EquipmentManager] No equipment file found for " + player.getName() + ". Using default.");
            return data;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            for (EquipmentType type : EquipmentType.values()) {
                if (json.has(type.name())) {
                    ItemStack item = ItemSerializer.fromBase64(json.get(type.name()).getAsString());
                    data.setItem(type, item);
                }
            }
            Bukkit.getLogger().info("[EquipmentManager] Loaded equipment for " + player.getName());
        } catch (IOException e) {
            Bukkit.getLogger().severe("[EquipmentManager] Failed to load equipment for " + player.getName());
            e.printStackTrace();
        }

        // lưu lại vào map
        equipmentMap.put(uuid, data);
        return data;
    }

    public PlayerEquipment getOrCreate(Player player) {
        return equipmentMap.computeIfAbsent(player.getUniqueId(), id -> new PlayerEquipment());
    }


}
