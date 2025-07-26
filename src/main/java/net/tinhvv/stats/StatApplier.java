package net.tinhvv.stats;// package net.tinhvv.stats;

import net.tinhvv.manager.StatManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class StatApplier {

    public static void apply(Player player, StatManager statManager) {
        AttributeInstance healthAttr = player.getAttribute(Attribute.MAX_HEALTH);
        if (healthAttr != null) {
            double maxHealth = 20.0 + statManager.getTotalStat(player, StatType.HEALTH);
            healthAttr.setBaseValue(maxHealth);
            player.setHealth(Math.min(player.getHealth(), maxHealth));
        }

        AttributeInstance armorAttr = player.getAttribute(Attribute.ARMOR);
        if (armorAttr != null) {
            armorAttr.setBaseValue(statManager.getTotalStat(player, StatType.TOUGHNESS));
        }

        // Có thể thêm SPEED, CRIT, v.v. sau
    }
}
