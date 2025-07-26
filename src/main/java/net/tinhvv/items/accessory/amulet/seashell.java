package net.tinhvv.items.accessory.amulet;

import net.tinhvv.equip.EquipmentType;
import net.tinhvv.items.AbstractCustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class seashell extends AbstractCustomItem {

    public seashell() {
        super("seashell", Material.CLAY_BALL, EquipmentType.AMULET.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.WHITE + "Seashell";
    }

    @Override
    protected List<String> getLore() {
        return List.of();
    }
}
