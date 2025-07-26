package net.tinhvv.stats;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VanillaArmorStats {

    public static List<StatModifier> getStats(ItemStack item, String source) {
        if (item == null || item.getType().isAir()) return List.of();

        return switch (item.getType()) {
            // 🟤 Leather Armor
            case LEATHER_HELMET -> List.of(new StatModifier(StatType.ARMOR, 1, source));
            case LEATHER_CHESTPLATE -> List.of(new StatModifier(StatType.ARMOR, 3, source));
            case LEATHER_LEGGINGS -> List.of(new StatModifier(StatType.ARMOR, 2, source));
            case LEATHER_BOOTS -> List.of(new StatModifier(StatType.ARMOR, 1, source));

            // 🔗 Chainmail Armor
            case CHAINMAIL_HELMET -> List.of(new StatModifier(StatType.ARMOR, 2, source));
            case CHAINMAIL_CHESTPLATE -> List.of(new StatModifier(StatType.ARMOR, 5, source));
            case CHAINMAIL_LEGGINGS -> List.of(new StatModifier(StatType.ARMOR, 4, source));
            case CHAINMAIL_BOOTS -> List.of(new StatModifier(StatType.ARMOR, 1, source));

            // 🪙 Golden Armor
            case GOLDEN_HELMET -> List.of(new StatModifier(StatType.ARMOR, 2, source));
            case GOLDEN_CHESTPLATE -> List.of(new StatModifier(StatType.ARMOR, 5, source));
            case GOLDEN_LEGGINGS -> List.of(new StatModifier(StatType.ARMOR, 3, source));
            case GOLDEN_BOOTS -> List.of(new StatModifier(StatType.ARMOR, 1, source));

            // ⚙ Iron Armor
            case IRON_HELMET -> List.of(new StatModifier(StatType.ARMOR, 2, source));
            case IRON_CHESTPLATE -> List.of(new StatModifier(StatType.ARMOR, 6, source));
            case IRON_LEGGINGS -> List.of(new StatModifier(StatType.ARMOR, 5, source));
            case IRON_BOOTS -> List.of(new StatModifier(StatType.ARMOR, 2, source));

            // 💎 Diamond Armor
            case DIAMOND_HELMET -> List.of(
                    new StatModifier(StatType.ARMOR, 3, source),
                    new StatModifier(StatType.TOUGHNESS, 2, source)
            );
            case DIAMOND_CHESTPLATE -> List.of(
                    new StatModifier(StatType.ARMOR, 8, source),
                    new StatModifier(StatType.TOUGHNESS, 2, source)
            );
            case DIAMOND_LEGGINGS -> List.of(
                    new StatModifier(StatType.ARMOR, 6, source),
                    new StatModifier(StatType.TOUGHNESS, 2, source)
            );
            case DIAMOND_BOOTS -> List.of(
                    new StatModifier(StatType.ARMOR, 3, source),
                    new StatModifier(StatType.TOUGHNESS, 2, source)
            );

            // 🟪 Netherite Armor
            case NETHERITE_HELMET -> List.of(
                    new StatModifier(StatType.ARMOR, 3, source),
                    new StatModifier(StatType.TOUGHNESS, 3, source)
            );
            case NETHERITE_CHESTPLATE -> List.of(
                    new StatModifier(StatType.ARMOR, 8, source),
                    new StatModifier(StatType.TOUGHNESS, 3, source)
            );
            case NETHERITE_LEGGINGS -> List.of(
                    new StatModifier(StatType.ARMOR, 6, source),
                    new StatModifier(StatType.TOUGHNESS, 3, source)
            );
            case NETHERITE_BOOTS -> List.of(
                    new StatModifier(StatType.ARMOR, 3, source),
                    new StatModifier(StatType.TOUGHNESS, 3, source)
            );

            // 🐢 Turtle Helmet
            case TURTLE_HELMET -> List.of(new StatModifier(StatType.ARMOR, 2, source));

            // ⚔ Melee Weapons
            case WOODEN_SWORD -> List.of(new StatModifier(StatType.STRENGTH, 4, source));
            case STONE_SWORD -> List.of(new StatModifier(StatType.STRENGTH, 5, source));
            case IRON_SWORD -> List.of(new StatModifier(StatType.STRENGTH, 6, source));
            case GOLDEN_SWORD -> List.of(new StatModifier(StatType.STRENGTH, 4, source));
            case DIAMOND_SWORD -> List.of(new StatModifier(StatType.STRENGTH, 7, source));
            case NETHERITE_SWORD -> List.of(new StatModifier(StatType.STRENGTH, 8, source));

// 🪓 Axes (used as weapons)
            case WOODEN_AXE -> List.of(new StatModifier(StatType.STRENGTH, 7, source));
            case STONE_AXE -> List.of(new StatModifier(StatType.STRENGTH, 9, source));
            case IRON_AXE -> List.of(new StatModifier(StatType.STRENGTH, 9, source));
            case GOLDEN_AXE -> List.of(new StatModifier(StatType.STRENGTH, 7, source));
            case DIAMOND_AXE -> List.of(new StatModifier(StatType.STRENGTH, 9, source));
            case NETHERITE_AXE -> List.of(new StatModifier(StatType.STRENGTH, 10, source));

// 🛠 Tools (for fun / compatibility)
            case WOODEN_PICKAXE -> List.of(new StatModifier(StatType.STRENGTH, 2, source));
            case STONE_PICKAXE -> List.of(new StatModifier(StatType.STRENGTH, 3, source));
            case IRON_PICKAXE -> List.of(new StatModifier(StatType.STRENGTH, 4, source));
            case GOLDEN_PICKAXE -> List.of(new StatModifier(StatType.STRENGTH, 2, source));
            case DIAMOND_PICKAXE -> List.of(new StatModifier(StatType.STRENGTH, 5, source));
            case NETHERITE_PICKAXE -> List.of(new StatModifier(StatType.STRENGTH, 6, source));

            case WOODEN_SHOVEL -> List.of(new StatModifier(StatType.STRENGTH, 1.5, source));
            case STONE_SHOVEL -> List.of(new StatModifier(StatType.STRENGTH, 2.5, source));
            case IRON_SHOVEL -> List.of(new StatModifier(StatType.STRENGTH, 3.5, source));
            case GOLDEN_SHOVEL -> List.of(new StatModifier(StatType.STRENGTH, 1.5, source));
            case DIAMOND_SHOVEL -> List.of(new StatModifier(StatType.STRENGTH, 4.5, source));
            case NETHERITE_SHOVEL -> List.of(new StatModifier(StatType.STRENGTH, 5.5, source));

            case WOODEN_HOE -> List.of(new StatModifier(StatType.STRENGTH, 1, source));
            case STONE_HOE -> List.of(new StatModifier(StatType.STRENGTH, 1, source));
            case IRON_HOE -> List.of(new StatModifier(StatType.STRENGTH, 2, source));
            case GOLDEN_HOE -> List.of(new StatModifier(StatType.STRENGTH, 1, source));
            case DIAMOND_HOE -> List.of(new StatModifier(StatType.STRENGTH, 3, source));
            case NETHERITE_HOE -> List.of(new StatModifier(StatType.STRENGTH, 4, source));

// 🛡 Special
            case TRIDENT -> List.of(new StatModifier(StatType.STRENGTH, 8, source));


            default -> List.of();
        };
    }
}
