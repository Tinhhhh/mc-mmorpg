package net.tinhvv.items.equipment.accessory.amulet;

import net.tinhvv.equip.EquipmentType;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Seashell extends AbstractCustomItem {

    public Seashell() {
        super("seashell", Material.CLAY_BALL, EquipmentType.AMULET.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.WHITE + "Seashell";
    }

    @Override
    protected List<String> getLore() {
        return StatFormat.lore(List.of(
                ChatColor.GRAY + "A beautiful Seashell,",
                ChatColor.GRAY + "often found on beaches.",
                ChatColor.GRAY + "Increases your luck when worn.",
                "",
                new StatModifier(StatType.HEALTH, 10.0, getId()),
                new StatModifier(StatType.STRENGTH, 5.0, getId()),
                "",
                ChatColor.WHITE + "" + ChatColor.BOLD + "Amulet"
        ));
    }

    @Override
    public Map<StatType, Double> getBaseStats() {
        return Map.of(
                StatType.HEALTH, 10.0,
                StatType.STRENGTH, 5.0
        );
    }
}
