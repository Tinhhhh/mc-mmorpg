package net.tinhvv.listeners.click;

import net.tinhvv.listeners.EventRouter;
import net.tinhvv.manager.StatManager;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatItemParser;
import net.tinhvv.stats.StatModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EquipmentStatRouter implements EventRouter<InventoryClickEvent> {

    @Override
    public boolean accept(InventoryClickEvent event) {
        return event.getWhoClicked() instanceof Player &&
                event.getSlotType() == InventoryType.SlotType.ARMOR;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        // Delay 1 tick để item được update
        Bukkit.getScheduler().runTaskLater(Mmorpg.getInstance(), () -> {
            Mmorpg.getStatManager().calculatePlayerStats(player);
        }, 1L);
    }

}
