package net.tinhvv.stats;// package net.tinhvv.stats;

import net.tinhvv.manager.StatManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StatApplier {

    public static void apply(Player player, StatManager statManager) {

        AttributeInstance healthAttr = player.getAttribute(Attribute.MAX_HEALTH);
        healthAttr.setBaseValue(statManager.getTotalStat(player, StatType.HEALTH));

        int amp = calculateRegenAmplifier(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue(),
                statManager.getTotalStat(player, StatType.REGENERATION));

        int durationTicks = Integer.MAX_VALUE; // gần như vĩnh viễn
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, durationTicks, amp, true, false));

        AttributeInstance armorAttr = player.getAttribute(Attribute.ARMOR);
        if (armorAttr != null) {
            armorAttr.setBaseValue(statManager.getTotalStat(player, StatType.TOUGHNESS));
        }

        AttributeInstance toughnessAttr = player.getAttribute(Attribute.ARMOR_TOUGHNESS);
        if (toughnessAttr != null) {
            toughnessAttr.setBaseValue(statManager.getTotalStat(player, StatType.TOUGHNESS));
        }

        AttributeInstance attackSpeedAttr = player.getAttribute(Attribute.ATTACK_SPEED);
        if (attackSpeedAttr != null) {
            double bonus = statManager.getTotalStat(player, StatType.ATTACK_SPEED);
            attackSpeedAttr.setBaseValue(statManager.getTotalStat(player, StatType.ATTACK_SPEED));
        }

        AttributeInstance speedAttr = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedAttr.setBaseValue(statManager.getTotalStat(player, StatType.SPEED));
        }

        // Có thể thêm SPEED, CRIT, v.v. sau
    }

    private static int calculateRegenAmplifier(double maxHealth, double healthRegen) {
        double base = (1.5 + maxHealth / 100.0) * (healthRegen / 100.0);
        double amplifier = 2.5 * base - 1;

        return Math.max(0, (int) amplifier); // Clamp để tránh âm
    }
}
