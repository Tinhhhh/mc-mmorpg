package net.tinhvv.listeners;

import net.tinhvv.items.Misc.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Gọi lệnh /ee như thể player tự gọi
        player.performCommand("ee");

        ItemStack item = new MenuItem().create();
        player.getInventory().setItem(8, item);
    }
}
