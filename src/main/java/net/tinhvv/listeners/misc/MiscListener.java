package net.tinhvv.listeners.misc;

import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.logging.Logger;

public class MiscListener implements Listener {

    private static final Logger LOGGER = Logger.getLogger(MiscListener.class.getName());

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.blockList().clear(); // Xóa danh sách block sẽ bị phá
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true); // Hủy mất độ bền với mọi vật phẩm
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setCancelled(true);
            player.setFoodLevel(20);
            player.setSaturation(20f);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Respawn sau 1 tick
        Bukkit.getScheduler().runTaskLater(Mmorpg.getInstance(), () -> {
            player.spigot().respawn(); // Yêu cầu Spigot auto respawn
        }, 1L); // Delay 1 tick để server kịp xử lý death
    }

}
