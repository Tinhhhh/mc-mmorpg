package net.tinhvv.items.misc;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class MenuItem extends AbstractCustomItem {

    public MenuItem() {
        super("menu", Material.NETHER_STAR, "menu_item");
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.WHITE + "" + ChatColor.BOLD + "Menu";
    }

    @Override
    protected List<String> getLore() {
        return List.of("Click to open the menu");
    }

    @Override
    public void onInteract(Player player, PlayerInteractEvent event) {
        callMenu(player);
        event.setCancelled(true);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        callMenu(player);
        event.setCancelled(true);
    }

    private void callMenu(Player player) {
        Bukkit.getScheduler().runTaskLater(Mmorpg.getInstance(), () -> {
            player.performCommand("menu");
        }, 1L);
    }
}
