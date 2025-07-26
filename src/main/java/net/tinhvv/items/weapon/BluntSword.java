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

public class BluntSword extends AbstractCustomItem {

    public BluntSword() {
        super("blunt_sword", Material.IRON_SWORD, ItemType.WEAPON_MELEE.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GRAY + "Blunt Sword";
    }

    @Override
    protected List<String> getLore() {
        return StatFormat.lore(List.of(
                ChatColor.LIGHT_PURPLE + "A Chinese sword, characterized by its curved blade and often used for martial arts practice",
                new StatModifier(StatType.BASE_DAMAGE, 5.5, getId()),
                new StatModifier(StatType.STRENGTH, 2.0, getId()),
                ChatColor.WHITE + "" + ChatColor.BOLD + "Sword"
        ));
    }

    @Override
    protected Map<StatType, Double> getBaseStats() {
        return Map.of(
                StatType.BASE_DAMAGE, 5.0,
                StatType.STRENGTH, 2.0
        );
    }
}
