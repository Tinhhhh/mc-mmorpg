package net.tinhvv.equip;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.tinhvv.mmorpg.Mmorpg;
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

    public static PlayerEquipment get(Player player) {
        return equipmentMap.computeIfAbsent(player.getUniqueId(), uuid -> load(player));
    }

    public static void save(Player player) {
        PlayerEquipment data = equipmentMap.get(player.getUniqueId());
        File file = new File(Mmorpg.getInstance().getDataFolder(), "equipment/" + player.getUniqueId() + ".json");
        file.getParentFile().mkdirs();

        JsonObject json = new JsonObject();
        for (Map.Entry<EquipmentType, ItemStack> entry : data.getAllEquipment().entrySet()) {
            json.add(entry.getKey().name(), ItemSerializer.toJson(entry.getValue()));
        }

        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerEquipment load(Player player) {
        File file = new File(Mmorpg.getInstance().getDataFolder(), "equipment/" + player.getUniqueId() + ".json");
        PlayerEquipment data = new PlayerEquipment();
        if (!file.exists()) return data;

        try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            for (EquipmentType type : EquipmentType.values()) {
                if (json.has(type.name())) {
                    data.setItem(type, ItemSerializer.fromJson(json.get(type.name()).getAsJsonObject()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
