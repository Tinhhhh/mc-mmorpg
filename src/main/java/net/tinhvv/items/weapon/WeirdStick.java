package net.tinhvv.items.weapon;

import net.tinhvv.equip.ItemType;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.stats.StatFormat;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class WeirdStick extends AbstractCustomItem {

    public WeirdStick() {
        super("weird_stick", Material.BLAZE_ROD, ItemType.WEAPON_MELEE.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.YELLOW + "A Weird Stick";
    }

    @Override
    protected List<String> getLore() {
        return StatFormat.lore(List.of(
                ChatColor.LIGHT_PURPLE + "A stick that feels strange to hold",
                new StatModifier(StatType.BASE_DAMAGE, 30.5, getId()),
                new StatModifier(StatType.STRENGTH, 10000.0, getId()),
                ChatColor.YELLOW + "" + ChatColor.BOLD + "Sword"
        ));
    }

    @Override
    public Map<StatType, Double> getBaseStats() {
        return Map.of(
                StatType.BASE_DAMAGE, 30.5,
                StatType.STRENGTH, 10000.0
        );
    }
}
