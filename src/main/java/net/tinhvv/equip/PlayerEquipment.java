package net.tinhvv.equip;

import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public class PlayerEquipment {
    private final Map<EquipmentType, ItemStack> equipment = new EnumMap<>(EquipmentType.class);

    public void setItem(EquipmentType type, ItemStack item) {
        equipment.put(type, item);
    }

    public ItemStack getItem(EquipmentType type) {
        return equipment.getOrDefault(type, null);
    }

    public Map<EquipmentType, ItemStack> getAllEquipment() {
        return equipment;
    }
}
