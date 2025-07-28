package net.tinhvv.items.equipment.accessory.gloves;

import net.tinhvv.equip.EquipmentType;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class InfinityGauntlet extends AbstractCustomItem {

    public InfinityGauntlet() {
        super("infinity_gauntlet", Material.HONEYCOMB, EquipmentType.GLOVES.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.YELLOW + "" + ChatColor.BOLD + "Infinity Gauntlet";
    }

    @Override
    protected List<String> getLore() {
        return StatFormat.lore(List.of(
                ChatColor.RED + "You can't find it",
                "",
                new StatModifier(StatType.ATTACK_SPEED, 200.0f, getId()),
                "",
                ChatColor.RED + "" + ChatColor.BOLD + "GLOVES"
        ));
    }

    @Override
    public Map<StatType, Double> getBaseStats() {
        return Map.of(
                StatType.ATTACK_SPEED, 200.0
        );
    }
}
