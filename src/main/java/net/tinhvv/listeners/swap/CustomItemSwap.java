package net.tinhvv.listeners.swap;

import net.tinhvv.listeners.EventRouter;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemSwap implements EventRouter<PlayerSwapHandItemsEvent> {
    @Override
    public boolean accept(PlayerSwapHandItemsEvent event) {
        return Mmorpg.getCustomItemManager().match(event.getOffHandItem()).isPresent();
    }

    @Override
    public void handle(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack current = event.getOffHandItem();

        Mmorpg.getCustomItemManager().match(current).ifPresent(item -> {
            switch (item.getId()) {
                case "menu" -> handleMenuItemClick(player, event);
            }
        });
    }

    private void handleMenuItemClick(Player player, PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }


}
