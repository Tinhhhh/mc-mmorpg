package net.tinhvv.items.equipment.armor.boots;

import net.tinhvv.equip.ItemType;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class ConverseC70 extends AbstractCustomItem {

    public ConverseC70() {
        super("converse_c70", Material.LEATHER_BOOTS, ItemType.BOOTS.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.WHITE + "" + ChatColor.BOLD + "Converse Chuck 70";
    }

    @Override
    protected List<String> getLore() {
        return StatFormat.lore(List.of(ChatColor.WHITE + "Converse Chuck 70 is classic",
                "",
                new StatModifier(StatType.SPEED, 0.2, getId()),
                "",
                ChatColor.GRAY + "Boots"
        ));
    }

    @Override
    public Map<StatType, Double> getBaseStats() {
        return Map.of(
                StatType.SPEED, 0.02,
                StatType.ARMOR, 2.0,
                StatType.TOUGHNESS, 0.5
        );
    }
}
