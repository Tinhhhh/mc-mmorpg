package net.tinhvv.listeners;

import net.tinhvv.items.misc.MenuItem;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
//        Mmorpg.getStatManager().load(event.getPlayer());
//        Mmorpg.getEquipmentManager().load(event.getPlayer());
        Mmorpg.getStatManager().updateFromAllEquipment(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Mmorpg.getStatManager().unload(event.getPlayer());
//        Mmorpg.getStatManager().save(event.getPlayer());
//        Mmorpg.getEquipmentManager().save(event.getPlayer());
    }
}
