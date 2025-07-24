package net.tinhvv.listeners.swap;

import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.listeners.EventRouter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemSwap implements EventRouter<PlayerSwapHandItemsEvent> {
    @Override
    public boolean accept(PlayerSwapHandItemsEvent event) {
        return CustomItemRegistry.match(event.getOffHandItem()).isPresent();
    }

    @Override
    public void handle(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack current = event.getOffHandItem();

        CustomItemRegistry.match(current).ifPresent(item -> {
            switch (item.getId()) {
                case "menu" -> handleMenuItemClick(player, event);
            }
        });
    }

    private void handleMenuItemClick(Player player, PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }


}
