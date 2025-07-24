package net.tinhvv.listeners.Drop;

import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.listeners.EventRouter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemDrop implements EventRouter<PlayerDropItemEvent> {

    @Override
    public boolean accept(PlayerDropItemEvent event) {
        return CustomItemRegistry.match(event.getItemDrop().getItemStack()).isPresent();
    }

    @Override
    public void handle(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack current = event.getItemDrop().getItemStack();

        CustomItemRegistry.match(current).ifPresent(item -> {
            switch (item.getId()) {
                case "menu" -> handleMenuItemClick(player, event);
            }
        });
    }

    private void handleMenuItemClick(Player player, PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
