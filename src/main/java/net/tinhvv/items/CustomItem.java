package net.tinhvv.items;

import org.bukkit.inventory.ItemStack;

public interface CustomItem {
    ItemStack create();
    boolean isMatch(ItemStack item);
    String getId();
    String getTagName();
}
