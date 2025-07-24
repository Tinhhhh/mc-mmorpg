package net.tinhvv.listeners.interact;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.listeners.EventRouter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class CustomItemInteract implements EventRouter<PlayerInteractEvent> {

    private static final Logger LOGGER = Logger.getLogger(CustomItemInteract.class.getName());

    @Override
    public boolean accept(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return false;

        return CustomItemRegistry.match(item).isPresent();
    }

    @Override
    public void handle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack current = event.getItem();

        CustomItemRegistry.match(current).ifPresent(item -> {
            switch (item.getId()) {
                case "goblin_egg" -> handleGoblinEgg(player, event);
                case "menu" -> handleMenuItem(player, event);
            }
        });
    }

    private void handleGoblinEgg(Player player, PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        CustomItemRegistry.match(item).ifPresent(customItem -> {
            if (customItem instanceof AbstractCustomItem i) {
                Action action = event.getAction();
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    i.onInteract(player, event);
                }
            }
        });
        event.setCancelled(true);
    }

    private void handleMenuItem(Player player, PlayerInteractEvent event) {


        ItemStack item = event.getItem();
        CustomItemRegistry.match(item).ifPresent(customItem -> {
            if (customItem instanceof AbstractCustomItem i) {
                Action action = event.getAction();
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    i.onInteract(player, event);
                }
            }
        });
        event.setCancelled(true);

    }
}
