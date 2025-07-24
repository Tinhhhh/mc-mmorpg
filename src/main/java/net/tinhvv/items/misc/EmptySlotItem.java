package net.tinhvv.items.misc;

import net.tinhvv.items.AbstractCustomItem;
import org.bukkit.Material;

import java.util.List;

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
