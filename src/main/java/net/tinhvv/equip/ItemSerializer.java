package net.tinhvv.equip;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import java.util.Map;

public class ItemSerializer {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject toJson(ItemStack item) {
        Map<String, Object> serialized = item.serialize();
        JsonElement jsonElement = gson.toJsonTree(serialized);
        return jsonElement.getAsJsonObject();
    }

    public static ItemStack fromJson(JsonObject json) {
        Map<String, Object> map = gson.fromJson(json, Map.class);
        // Gson sẽ convert số nguyên về Double nên cần fix
        return ItemStack.deserialize(fixNumbers(map));
    }

    // ⚠️ Gson auto convert int -> double, nên cần fix:
    @SuppressWarnings("unchecked")
    private static Map<String, Object> fixNumbers(Map<String, Object> map) {
        map.replaceAll((k, v) -> {
            if (v instanceof Double d) {
                if (d % 1 == 0) return d.intValue();
            } else if (v instanceof Map) {
                return fixNumbers((Map<String, Object>) v);
            }
            return v;
        });
        return map;
    }
}
