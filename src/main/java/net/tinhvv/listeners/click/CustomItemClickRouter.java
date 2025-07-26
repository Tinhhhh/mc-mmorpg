package net.tinhvv.listeners.click;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.manager.CustomItemManager;
import net.tinhvv.listeners.EventRouter;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class CustomItemClickRouter implements EventRouter<InventoryClickEvent> {

    private static final Logger LOGGER = Logger.getLogger(CustomItemClickRouter.class.getName());

    @Override
    public boolean accept(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return false;

        return Mmorpg.getCustomItemManager().match(item).isPresent();
    }

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        Mmorpg.getCustomItemManager().match(current).ifPresent(item -> {
            switch (item.getId()) {
                case "goblin_egg" -> handleGoblinEgg(player, event);
                case "stats" -> handleStats(player, event);
            }
        });
    }

    private void handleGoblinEgg(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Mmorpg.getCustomItemManager().match(item).ifPresent(customItem -> {
            if (customItem instanceof AbstractCustomItem i) {
                i.onClick(player, event);
            }
        });

    }

    private void handleStats(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        player.performCommand("stats");
    }
}
