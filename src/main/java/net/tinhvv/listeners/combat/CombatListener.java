package net.tinhvv.listeners.combat;

import net.tinhvv.equip.ItemType;
import net.tinhvv.mmorpg.Mmorpg;
import net.tinhvv.stats.StatModifier;
import net.tinhvv.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
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

        Mmorpg.getStatManager().calculatePlayerStats(attacker);

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

        //Kiểm tra tốc độ đánh
        double baseAtkSpeed = 1;
        double bonusAtkSpeed = Mmorpg.getStatManager().getTotalFromOneStat(attacker, StatType.ATTACK_SPEED);
        double totalAtkSpeed = baseAtkSpeed * (1 + bonusAtkSpeed / 100.0); // Tính toán tổng Attack Speed

        long now = System.currentTimeMillis();
        long lastAttack = lastAttackTime.getOrDefault(attacker.getUniqueId(), 0L);
        double delay = 1000.0 / totalAtkSpeed; // ms per hit

        if (now - lastAttack < delay) {
            event.setCancelled(true); // Huỷ animation / chặn đánh
        } else {
            lastAttackTime.put(attacker.getUniqueId(), now);
            LOGGER.info("[CombatListener] " + attacker.getName() + " đang tấn công với bonus Attack Speed: " + bonusAtkSpeed);
            LOGGER.info("[CombatListener] " + attacker.getName() + " đã tấn công với base Attack Speed: " + baseAtkSpeed);
            LOGGER.info("[CombatListener] " + attacker.getName() + " đã tấn công với total atk speed: " + delay / 1000 + "s/hit");

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
//        LOGGER.info("[totalStrength: " + totalStrength);
        }

    }

    public static boolean isCrit(double critChancePercent) {
        double roll = Math.random() * 100; // random từ 0.0 đến <100.0
        return roll < critChancePercent;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(Mmorpg.getInstance(), () -> {

        }, 1L); // delay 1 tick để đảm bảo item đã được chuyển xong
    }

//    private void updateAttackSpeed(Player player) {
//        ItemStack item = player.getInventory().getItemInMainHand();
//
//        // Nếu tay không
//        if (item == null || item.getType().isAir() || !Mmorpg.getStatManager().isWeapon(item)) {
//            StatApplier.applyAtkSpeed(player, 4.0); // mặc định Minecraft
//            return;
//        }
//
//        List<StatModifier> itemModifiers = Mmorpg.getCustomItemManager().getModifiersByItemStack(item);
//        // Lấy từ attribute nếu có sẵn trong item
//        double atkSpeed = StatModifier.getValueByStatType(itemModifiers, StatType.ATTACK_SPEED);
//        if (atkSpeed == 0.0f) {
//            atkSpeed = 1.0f; // Nếu không có modifier, dùng giá trị mặc định
//        }
//
//        double bonusAttackSpeed = Mmorpg.getStatManager().getTotalFromOneStat(player, StatType.ATTACK_SPEED);
//        LOGGER.info("[CombatListener] Tính toán Attack Speed cho " + player.getName() + ": " + atkSpeed + " (base) + " + bonusAttackSpeed + " (bonus)");
//
//        double finalAttackSpeed = atkSpeed + bonusAttackSpeed;
//        StatApplier.applyAtkSpeed(player, finalAttackSpeed);
//        LOGGER.info("[CombatListener] Cập nhật Attack Speed cho " + player.getName() + ": " + finalAttackSpeed);
//    }

    @EventHandler
    public void onSwitchItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        // Slot vừa được đổi sang: event.getNewSlot()
        // Item hiện tại: player.getInventory().getItem(event.getNewSlot());
        Mmorpg.getStatManager().calculatePlayerStats(player);
    }

}
