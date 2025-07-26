package net.tinhvv.items.weapon;

import net.tinhvv.equip.ItemType;
import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.stats.StatFormat;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class RemoveEntitySword extends AbstractCustomItem {

    public RemoveEntitySword() {
        super("remove_entity_sword", Material.WOODEN_SWORD, ItemType.WEAPON_MELEE.toString());
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.RED + "[Admin] Remove Entity Sword";
    }

    @Override
    protected List<String> getLore() {
        return StatFormat.lore(List.of(
                ChatColor.WHITE + "This sword removes any entities",
                ChatColor.RED + "" + ChatColor.BOLD + "Sword"
        ));
    }
}
