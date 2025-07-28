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
        this.displayName = ChatColor.GRAY + "Blunt Sword";
        this.lore = StatFormat.lore(List.of(
                new StatModifier(StatType.BASE_DAMAGE, 5.5, getId()),
                new StatModifier(StatType.STRENGTH, 2.0, getId()),
                "",
                "",

                ChatColor.LIGHT_PURPLE + "A Chinese sword with a blunt edge",
                ChatColor.WHITE + "" + ChatColor.BOLD + "Sword"
        ));
        this.baseStats.putAll(Map.of(
                StatType.BASE_DAMAGE, 5.0,
                StatType.STRENGTH, 2.0
        ));
    }
}
