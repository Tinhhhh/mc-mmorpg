package net.tinhvv.listeners.misc;

import net.tinhvv.equip.ItemType;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
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


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        ItemMeta meta = weapon.getItemMeta();
        NamespacedKey key = new NamespacedKey(Mmorpg.getInstance(), ItemType.WEAPON_MELEE.toString());
        if (meta != null && meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if ("remove_entity_sword".equalsIgnoreCase(id)) {
                if (event.getEntity() instanceof LivingEntity victim) {
                    victim.setHealth(0); // Giết ngay lập tức
                }
            }
        }

        // Tính damage mới tại đây
        double totalStrength = Mmorpg.getStatManager().getTotalStat(attacker, StatType.STRENGTH);
        double totalCritChance = Mmorpg.getStatManager().getTotalStat(attacker, StatType.CRIT_CHANCE);
        double totalCritDamage = Mmorpg.getStatManager().getTotalStat(attacker, StatType.CRIT_DAMAGE);

        // initialDMG = (5 + weaponDamage) * (1 + strength / 100)
        List<StatModifier> modifiers = Mmorpg.getCustomItemManager().getModifiers(weapon);
        double weaponDamage = StatModifier.getModifierByStatType(modifiers, StatType.BASE_DAMAGE) != null ? StatModifier.getModifierByStatType(modifiers, StatType.BASE_DAMAGE).getValue() : 0.0;
        double strength = StatModifier.getModifierByStatType(modifiers, StatType.STRENGTH) != null ? StatModifier.getModifierByStatType(modifiers, StatType.STRENGTH).getValue() : 0.0;

        double initialDamage = (5 + weaponDamage) * (1 + totalStrength / 100.0);

        // 1 + combatLevelBonus + Enchants + weaponBonus
        double dmgMultiplier = 1.0; // Đây là hệ số nhân tổng hợp từ nhiều nguồn

        // (initialDmg * dmgMultiplier) + (1 + ArmorBonus) x (1 + CritDamage / 100)


        int finalDamage = (int) (initialDamage * dmgMultiplier);

        if (isCrit(totalCritChance)) {
            // Nếu là crit, tính thêm crit damage
            finalDamage += (int) (finalDamage * (1 + totalCritDamage / 100.0));
        }

        // Set damage cho target
        event.setDamage(finalDamage);
        LOGGER.info("[" + attacker.getName() + "] đã tấn công " + target.getName() + " với damage: " + finalDamage);

    }

    public static boolean isCrit(double critChancePercent) {
        double roll = Math.random() * 100; // random từ 0.0 đến <100.0
        return roll < critChancePercent;
    }


}
