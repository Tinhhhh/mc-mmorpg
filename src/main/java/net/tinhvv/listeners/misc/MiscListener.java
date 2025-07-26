package net.tinhvv.listeners.misc;

import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class MiscListener implements Listener {

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
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        // Tính damage mới tại đây
        double strength = Mmorpg.getStatManager().getTotalStat(attacker, StatType.STRENGTH);
        double critChance = Mmorpg.getStatManager().getTotalStat(attacker, StatType.CRIT_CHANCE);
        double critDamage = Mmorpg.getStatManager().getTotalStat(attacker, StatType.CRIT_DAMAGE);

        double baseDamage = event.getDamage();
        double newDamage = baseDamage + strength;

    }


}
