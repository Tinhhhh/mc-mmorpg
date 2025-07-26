package net.tinhvv.listeners.click;

import net.tinhvv.listeners.EventRouter;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatItemParser;
import net.tinhvv.manager.StatManager;
import net.tinhvv.stats.StatModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EquipmentStatRouter implements EventRouter<InventoryClickEvent> {

    private final StatManager statManager;

    public EquipmentStatRouter(StatManager statManager) {
        this.statManager = statManager;
    }

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

            // Xử lý từng giáp
            updateSlot(player, "HEAD", player.getInventory().getHelmet());
            updateSlot(player, "CHEST", player.getInventory().getChestplate());
            updateSlot(player, "LEGS", player.getInventory().getLeggings());
            updateSlot(player, "FEET", player.getInventory().getBoots());

            // Áp dụng stat gameplay
//            StatApplier.apply(player, statManager);
        }, 1L);
    }

    private void updateSlot(Player player, String slotId, ItemStack item) {
        statManager.removeModifierBySource(player, "equip_" + slotId);
        if (item != null) {
            List<StatModifier> mods = StatItemParser.parse(item, "equip_" + slotId);
            for (StatModifier mod : mods) {
                statManager.addModifier(player, mod);
            }
        }
    }
}
