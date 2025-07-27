package net.tinhvv.listeners.combat;

import net.tinhvv.equip.ItemType;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class CombatListener implements Listener {
    private static final Logger LOGGER = Logger.getLogger(CombatListener.class.getName());
    private final Map<UUID, Long> lastAttackTime = new HashMap<>();


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
        double totalStrength = Mmorpg.getStatManager().getTotalFromOneStat(attacker, StatType.STRENGTH);
        double totalCritChance = Mmorpg.getStatManager().getTotalFromOneStat(attacker, StatType.CRIT_CHANCE);
        double totalCritDamage = Mmorpg.getStatManager().getTotalFromOneStat(attacker, StatType.CRIT_DAMAGE);

        // initialDMG = (5 + weaponDamage) * (1 + strength / 100)
        List<StatModifier> modifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(weapon);
        double weaponDamage = StatModifier.getModifierByStatType(modifiers, StatType.BASE_DAMAGE) != null ? StatModifier.getModifierByStatType(modifiers, StatType.BASE_DAMAGE).getValue() : 0.0;
        double strength = StatModifier.getModifierByStatType(modifiers, StatType.STRENGTH) != null ? StatModifier.getModifierByStatType(modifiers, StatType.STRENGTH).getValue() : 0.0;

        double initialDamage = (5 + weaponDamage) * (1 + totalStrength / 100.0);
        LOGGER.info("[" + attacker.getName() + "] tấn công " + target.getName() + " với initial damage: " + initialDamage);

        // 1 + combatLevelBonus + Enchants + weaponBonus
        double dmgMultiplier = 1.0; // Đây là hệ số nhân tổng hợp từ nhiều nguồn
        LOGGER.info("[" + attacker.getName() + "] tính toán dmgMultiplier: " + dmgMultiplier);

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

    // Khi player swing (chuột trái)
//    @EventHandler
//    public void onAttack(PlayerAnimationEvent event) {
//        Player player = event.getPlayer();
//        UUID uuid = player.getUniqueId();
//
//        ItemStack item = player.getInventory().getItemInMainHand();
//        double baseAtkSpeed = 4.0;
//        if (item != null) {
//            ItemMeta meta = item.getItemMeta();
//
//            return; // Không có item trên tay
//        }
//
//        double attackSpeed = Mmorpg.getStatManager().getTotalStat(player, StatType.ATTACK_SPEED); // ví dụ: 4.0
//
//        long now = System.currentTimeMillis();
//        long lastAttack = lastAttackTime.getOrDefault(player.getUniqueId(), 0L);
//        double delay = 1000.0 / attackSpeed; // ms per hit
//
//        if (now - lastAttack < delay) {
//            event.setCancelled(true); // Huỷ animation / chặn đánh
//        } else {
//            lastAttackTime.put(player.getUniqueId(), now);
//        }
//    }

    @EventHandler
    public void onSwitchItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Mmorpg.getStatManager().calculatePlayerStats(player);
    }



}
