package net.tinhvv.items.equipment.accessory.ring;

import net.tinhvv.equip.EquipmentType;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class OneRing extends AbstractCustomItem {
    public OneRing() {
        super("one_ring", Material.ENDER_EYE, EquipmentType.RING.toString());
        this.displayName = ChatColor.YELLOW + "" + ChatColor.BOLD + "Sauron's One Ring";
        this.lore = StatFormat.lore(List.of(
                ChatColor.RED + "A powerful ring created by Sauron",
                ChatColor.RED + "You can't find it",
                "",
                new StatModifier(StatType.STRENGTH, 5000.0, getId()),
                new StatModifier(StatType.CRIT_CHANCE, 100.0, getId()),
                new StatModifier(StatType.CRIT_DAMAGE, 5000.0, getId()),
                "",
                ChatColor.RED + "" + ChatColor.BOLD + "Ring"
        ));
        this.baseStats.putAll(Map.of(
                        StatType.STRENGTH, 5000.0,
                        StatType.CRIT_CHANCE, 100.0,
                        StatType.CRIT_DAMAGE, 5000.0
                )
        );
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.YELLOW + "" + ChatColor.BOLD + "Sauron's One Ring";

    }

//    @Override
//    protected List<String> getLore() {
//        return StatFormat.lore(List.of(
//                ChatColor.RED + "A powerful ring created by Sauron",
//                ChatColor.RED + "You can't find it",
//                "",
//                new StatModifier(StatType.STRENGTH, 5000.0, getId()),
//                new StatModifier(StatType.CRIT_CHANCE, 100.0, getId()),
//                new StatModifier(StatType.CRIT_DAMAGE, 5000.0, getId()),
//                "",
//                ChatColor.RED + "" + ChatColor.BOLD + "Ring"
//        ));
//    }
//
//    @Override
//    protected Map<StatType, Double> getBaseStats() {
//        return Map.of(
//                StatType.STRENGTH, 5000.0,
//                StatType.CRIT_CHANCE, 100.0,
//                StatType.CRIT_DAMAGE, 5000.0
//        );
//    }
}
