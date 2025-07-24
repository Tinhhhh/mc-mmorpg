package net.tinhvv.items.Misc;

import net.tinhvv.items.AbstractCustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class CloseItem extends AbstractCustomItem {

    public CloseItem() {
        super("close_item", Material.BARRIER, "close_item");
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.RED + "" + ChatColor.BOLD + "Close";
    }

    @Override
    protected List<String> getLore() {
        return List.of();
    }

}
