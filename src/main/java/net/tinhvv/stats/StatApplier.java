package net.tinhvv.stats;// package net.tinhvv.stats;

import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.logging.Logger;

public class StatApplier {

    private static final Logger LOGGER = Logger.getLogger(StatApplier.class.getName());

    public static void applyAllStats(Player player) {
        List<StatModifier> modifiers = Mmorpg.getStatManager().getModifiers(player);

        for (StatModifier mod : modifiers) {
            StatType type = mod.getStat();
            double value = mod.getValue();

            if (type.name().equalsIgnoreCase(StatType.ATTACK_SPEED.toString())) {
                LOGGER.info("[StatApplier] Applying " + type.name() + " with value " + value + " to player " + player.getName());
            }

            switch (type) {
                case HEALTH -> {
                    AttributeInstance health = player.getAttribute(Attribute.MAX_HEALTH);
                    if (health != null) health.setBaseValue(value);
                }
                case REGENERATION -> {
                    AttributeInstance health = player.getAttribute(Attribute.MAX_HEALTH);
                    double maxHealth = (health != null) ? health.getBaseValue() : 20.0;
                    int amp = calculateRegenAmplifier(maxHealth, value);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, amp, true, false));
                }
                case ARMOR -> {
                    AttributeInstance armor = player.getAttribute(Attribute.ARMOR);
                    if (armor != null) armor.setBaseValue(value);
                }
                case TOUGHNESS -> {
                    AttributeInstance tough = player.getAttribute(Attribute.ARMOR_TOUGHNESS);
                    if (tough != null) tough.setBaseValue(value);
                }
                case ATTACK_SPEED -> {
                    AttributeInstance atkSpeed = player.getAttribute(Attribute.ATTACK_SPEED);
                    if (atkSpeed != null) atkSpeed.setBaseValue(value);
                }
                case SPEED -> {
                    AttributeInstance speed = player.getAttribute(Attribute.MOVEMENT_SPEED);
                    if (speed != null) speed.setBaseValue(value);
                }
                // mấy chỉ số khác thì tự nhét thêm vô đây
            }
        }
    }

//    public static void applyAtkSpeed(Player player, double value) {
//        AttributeInstance atkSpeed = player.getAttribute(Attribute.ATTACK_SPEED);
//        if (atkSpeed != null) {
//            atkSpeed.setBaseValue(value);
//            LOGGER.info("[StatApplier] Applied ATTACK_SPEED: " + value + " to player " + player.getName());
//        } else {
//            LOGGER.warning("[StatApplier] ATTACK_SPEED attribute not found for player " + player.getName());
//        }
//    }

    private static int calculateRegenAmplifier(double maxHealth, double healthRegen) {
        double base = (1.5 + maxHealth / 100.0) * (healthRegen / 100.0);
        double amplifier = 2.5 * base - 1;

        return Math.max(0, (int) amplifier); // Clamp để tránh âm
    }
}
