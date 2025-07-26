package net.tinhvv.equip;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class ItemSerializer {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String toBase64(ItemStack item) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             BukkitObjectOutputStream out = new BukkitObjectOutputStream(byteOut)) {

            out.writeObject(item);
            return Base64.getEncoder().encodeToString(byteOut.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize item", e);
        }
    }

    public static ItemStack fromBase64(String base64) {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream in = new BukkitObjectInputStream(byteIn)) {

            return (ItemStack) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize item", e);
        }
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
