package net.tinhvv.manager;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItem;
import net.tinhvv.items.easterEgg.GoblinEgg;
import net.tinhvv.items.equipment.accessory.amulet.Seashell;
import net.tinhvv.items.equipment.armor.boots.ConverseC70;
import net.tinhvv.items.misc.EmptySlotItem;
import net.tinhvv.items.misc.MenuItem;
import net.tinhvv.items.misc.MissingItem;
import net.tinhvv.items.weapon.BluntSword;
import net.tinhvv.items.weapon.RemoveEntitySword;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatItemParser;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.VanillaItemStats;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Logger;

public class CustomItemManager {

    private static final Map<String, CustomItem> items = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(CustomItemManager.class.getName());

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

    public String getTagName(ItemStack item) {
        // Kiểm tra xem item có phải là CustomItem không
        Optional<CustomItem> match = match(item);
        if (match.isPresent()) {
            return match.get().getTagName(); // Trả về ID của CustomItem
        }
        return "UNKNOWN"; // Không xác định được loại item
    }

    public String getId(ItemStack item) {
        // Kiểm tra xem item có phải là CustomItem không
        Optional<CustomItem> match = match(item);
        if (match.isPresent()) {
            return match.get().getId(); // Trả về ID của CustomItem
        }
        return "UNKNOWN"; // Không xác định được loại item
    }

    public List<StatModifier> getModifiersByItemStack(ItemStack item) {

        //Mặc định không có modifier nào
        List<StatModifier> modifiers = new ArrayList<>();

        String itemSource = StatItemParser.getItemSource(item);
        if (itemSource.startsWith("VANILLA:")) {
            // Nếu không phải CustomItem thì kiểm tra xem có phải item vanilla không
            modifiers = VanillaItemStats.getStats(item, itemSource);
        }

        // Nếu là CustomItem thì lấy các modifier từ item đó
        if (itemSource.startsWith("CUSTOM:")) {
            Optional<CustomItem> custom = Mmorpg.getCustomItemManager().match(item);
            if (custom.isPresent() && custom.get() instanceof AbstractCustomItem aci) {
                modifiers = aci.getStatModifiers();
            }
        }

        LOGGER.info("modifiers: " + modifiers);
        return modifiers;
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
        register(new ConverseC70());
        // Thêm các item khác ở đây
    }
}
