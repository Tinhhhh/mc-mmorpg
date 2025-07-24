
package net.tinhvv.equip;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EquipmentData {
    private final Map<EquipmentSlot, ItemStack> equippedItems = new HashMap<>();

    public void equip(EquipmentSlot slot, ItemStack item) {
        equippedItems.put(slot, item);
    }

    public ItemStack get(EquipmentSlot slot) {
        return equippedItems.getOrDefault(slot, null);
    }

    public void unequip(EquipmentSlot slot) {
        equippedItems.remove(slot);
    }
}
