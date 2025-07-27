package net.tinhvv.listeners.drop;

import net.tinhvv.listeners.EventRouter;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemDrop implements EventRouter<PlayerDropItemEvent> {

    @Override
    public boolean accept(PlayerDropItemEvent event) {
        return Mmorpg.getCustomItemManager().match(event.getItemDrop().getItemStack()).isPresent();
    }

    @Override
    public void handle(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack current = event.getItemDrop().getItemStack();

        Mmorpg.getCustomItemManager().match(current).ifPresent(item -> {
            switch (item.getId()) {
                case "menu" -> handleMenuItemClick(player, event);
            }
        });
    }

    private void handleMenuItemClick(Player player, PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
