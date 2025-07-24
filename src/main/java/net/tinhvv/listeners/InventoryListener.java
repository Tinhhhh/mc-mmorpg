package net.tinhvv.listeners;

import net.tinhvv.listeners.click.CustomItemClickRouter;
import net.tinhvv.listeners.click.EquipmentClickRouter;
import net.tinhvv.listeners.drop.CustomItemDrop;
import net.tinhvv.listeners.interact.CustomItemInteract;
import net.tinhvv.listeners.swap.CustomItemSwap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;

public class InventoryListener implements Listener {

    private final List<EventRouter<InventoryClickEvent>> clickRouters = List.of(
            new CustomItemClickRouter(),
            new EquipmentClickRouter()
            // add more
    );

    private final List<EventRouter<PlayerDropItemEvent>> dropRouters = List.of(
            new CustomItemDrop()
            // add more
    );

    private final List<EventRouter<PlayerInteractEvent>> interactRouters = List.of(
            new CustomItemInteract()
            // add more
    );

    private final List<EventRouter<PlayerSwapHandItemsEvent>> swapRouters = List.of(
            new CustomItemSwap()
            // add more
    );

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        for (EventRouter<InventoryClickEvent> router : clickRouters) {
            if (router.accept(event)) {
                router.handle(event);
                break;
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        for (EventRouter<PlayerDropItemEvent> router : dropRouters) {
            if (router.accept(event)) {
                router.handle(event);
                break;
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        for (EventRouter<PlayerInteractEvent> router : interactRouters) {
            if (router.accept(event)) {
                router.handle(event);
                break;
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        for (EventRouter<PlayerSwapHandItemsEvent> router : swapRouters) {
            if (router.accept(event)) {
                router.handle(event);
                break;
            }
        }
    }

}
