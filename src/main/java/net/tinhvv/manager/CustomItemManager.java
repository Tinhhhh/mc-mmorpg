package net.tinhvv.manager;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItem;
import net.tinhvv.items.accessory.amulet.Seashell;
import net.tinhvv.items.easterEgg.GoblinEgg;
import net.tinhvv.items.misc.EmptySlotItem;
import net.tinhvv.items.misc.MenuItem;
import net.tinhvv.items.misc.MissingItem;
import net.tinhvv.items.weapon.BluntSword;
import net.tinhvv.items.weapon.RemoveEntitySword;
import net.tinhvv.stats.StatModifier;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomItemManager {

    private static final Map<String, CustomItem> items = new HashMap<>();

    /**
     * Đăng ký một CustomItem mới vào hệ thống
     *
     * @param item CustomItem cần đăng ký
     */
    public void register(CustomItem item) {
        items.put(item.getId(), item); // đảm bảo mỗi ID là unique
    }

    /*
    Lấy ra CustomItem tương ứng từ ItemStack
     */
    public Optional<CustomItem> match(ItemStack item) {
        return items.values().stream().filter(ci -> ci.isMatch(item)).findFirst();
    }

    /**
     * Lấy CustomItem theo ID
     *
     * @param id ID của CustomItem cần lấy
     * @return CustomItem nếu tìm thấy, null nếu không tìm thấy
     */
    public CustomItem getById(String id) {
        return items.get(id);
    }

    public Collection<CustomItem> getAll() {
        return items.values();
    }

    public List<StatModifier> getModifiers(ItemStack item) {
        Optional<CustomItem> match = match(item);
        if (match.isPresent() && match.get() instanceof AbstractCustomItem customItem) {
            return customItem.getStatModifiers();
        }
        return List.of(); // không có modifier
    }


    // Gọi hàm này trong onEnable()
    public void init() {
        register(new GoblinEgg());
        register(new MenuItem());
        register(new EmptySlotItem());
        register(new MissingItem());
        register(new Seashell());
        register(new RemoveEntitySword());
        register(new BluntSword());
        // Thêm các item khác ở đây
    }
}
