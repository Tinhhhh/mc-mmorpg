package net.tinhvv.items;

import net.tinhvv.items.accessory.amulet.seashell;
import net.tinhvv.items.easterEgg.GoblinEgg;
import net.tinhvv.items.misc.EmptySlotItem;
import net.tinhvv.items.misc.MenuItem;
import net.tinhvv.items.misc.MissingItem;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomItemRegistry {

    private static final Map<String, CustomItem> items = new HashMap<>();

    public static void register(CustomItem item) {
        items.put(item.getId(), item); // đảm bảo mỗi ID là unique
    }

    public static Optional<CustomItem> match(ItemStack item) {
        return items.values().stream().filter(ci -> ci.isMatch(item)).findFirst();
    }

    public static CustomItem getById(String id) {
        return items.get(id);
    }

    public static Collection<CustomItem> getAll() {
        return items.values();
    }

    // Gọi hàm này trong onEnable()
    public static void init() {
        register(new GoblinEgg());
        register(new MenuItem());
        register(new EmptySlotItem());
        register(new MissingItem());
        register(new seashell());
        // Thêm các item khác ở đây
    }
}
