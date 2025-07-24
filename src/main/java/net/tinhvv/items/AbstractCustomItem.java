package net.tinhvv.items;

import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public abstract class AbstractCustomItem implements CustomItem {

    private final String id;
    private final Material material;
    private final NamespacedKey key;

    protected AbstractCustomItem(String id, Material material, String tagName) {
        this.id = id;
        this.material = material;
        this.key = new NamespacedKey(Mmorpg.getInstance(), tagName);
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());
        meta.setLore(getLore());
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
        item.setItemMeta(meta);
        return item;
    }

    public String getId() {
        return id;
    }

    public boolean isMatch(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return id.equals(value);
    }

    protected abstract String getDisplayName();

    protected abstract List<String> getLore();

    public void onInteract(Player player, PlayerInteractEvent event) {
        // Mặc định không làm gì
    }

    public void onClick(Player player, InventoryClickEvent event) {
        // Mặc định không làm gì
    }


}
