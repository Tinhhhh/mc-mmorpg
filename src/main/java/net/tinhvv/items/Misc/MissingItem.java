package net.tinhvv.items.Misc;

import net.tinhvv.items.AbstractCustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class MissingItem extends AbstractCustomItem {

    public MissingItem() {
        super("missing_item", Material.WHITE_STAINED_GLASS_PANE, "missing_item");
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.WHITE + "Missing Item";
    }

    @Override
    protected List<String> getLore() {
        return List.of();
    }

}
