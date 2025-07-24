package net.tinhvv.items.Misc;

import net.tinhvv.items.AbstractCustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EmptySlotItem extends AbstractCustomItem {

    public EmptySlotItem() {
        super("empty_slot", Material.BLACK_STAINED_GLASS_PANE, "empty_slot");
    }

    @Override
    protected String getDisplayName() {
        return "";
    }

    @Override
    protected List<String> getLore() {
        return List.of();
    }
}
