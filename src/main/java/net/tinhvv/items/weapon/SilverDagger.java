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

public class SilverDagger extends AbstractCustomItem {

    public SilverDagger() {
        super("silver_dagger", Material.IRON_SWORD, ItemType.WEAPON_MELEE.toString());
        this.displayName = ChatColor.GRAY + "Silver Dagger";
        this.lore = StatFormat.lore(List.of(
                new StatModifier(StatType.BASE_DAMAGE, 8.5, getId()),
                new StatModifier(StatType.STRENGTH, 10.0, getId()),
                new StatModifier(StatType.ATTACK_SPEED, 150.0, getId()),
                "",
                "",
                ChatColor.LIGHT_PURPLE + "A dagger made of silver",
                ChatColor.LIGHT_PURPLE + "Obtained from the lord Silver",
                ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Sword"
        ));
        this.baseStats.putAll(Map.of(
                StatType.BASE_DAMAGE, 8.5,
                StatType.STRENGTH, 10.0,
                StatType.ATTACK_SPEED, 150.0
        ));
    }
}
