package net.tinhvv.stats;

import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatFormat {

    public static List<String> lore(List<Object> lines) {
        return lines.stream()
                .map(line -> {
                    if (line instanceof StatModifier mod) {
                        StatType type = mod.getStat();
                        double value = mod.getValue();
                        String color = getColor(type);
                        String icon = getIcon(type);
                        String suffix = (type.name().contains("CRIT")) || (type.name().equalsIgnoreCase("ATTACK_SPEED")) ? "%" : "";


                        if (type == StatType.SPEED) {
                            value *= 100;
                        }

                        String formatted = (value % 1 == 0)
                                ? Integer.toString((int) value)
                                : String.format("%.2f", value);

                        return color + icon + " " + capitalize(type.name()) + " +" + formatted + suffix;
                    } else {
                        return line.toString();
                    }
                }).collect(Collectors.toList());
    }

    private static String getColor(StatType type) {
        return switch (type) {
            case STRENGTH -> "§4";
            case HEALTH, REGENERATION -> "§c";
            case ARMOR -> "§7";
            case ATTACK_SPEED -> "§e";
            case TOUGHNESS -> "§2";
            case LUCK -> "§5";
            case INTELLIGENT -> "§b";
            case CRIT_CHANCE, CRIT_DAMAGE -> "§9";
            case SPEED -> "§f";
            default -> "§7";
        };
    }

    private static String getIcon(StatType type) {
        return switch (type) {
            case STRENGTH -> "❁";
            case HEALTH -> "❤";
            case REGENERATION -> "💞";
            case ATTACK_SPEED -> "⚔";
            case ARMOR -> "🛡";
            case TOUGHNESS -> "❖";
            case LUCK -> "🍀";
            case INTELLIGENT -> "✦";
            case CRIT_CHANCE -> "💫";
            case CRIT_DAMAGE -> "☠";
            case SPEED -> "👟";
            default -> "-";
        };
    }

    private static String capitalize(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase().replace("_", " ");
    }

    public static String processStatPlaceholder(String input, Player player) {
        Matcher matcher = Pattern.compile("\\{stat:([A-Z_]+)}").matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            StatType type;
            try {
                type = StatType.valueOf(key);
            } catch (IllegalArgumentException e) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement("{invalid:" + key + "}"));
                continue;
            }

            double value = Mmorpg.getStatManager().getTotalFromOneStat(player, type);
            String replacement = format(player, type, value);
            if (replacement.isEmpty()) replacement = ""; // hoặc "{hidden}" nếu mày debug
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));

        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String format(Player player, StatType type, double statValue) {

        String color = getColor(type);
        String icon = getIcon(type);
        double value = statValue;

        boolean hasPercent = type.name().contains("CRIT") || type == StatType.ATTACK_SPEED;

        if (type == StatType.SPEED) {
            value *= 1000;
        }

        String formatted = (value % 1 == 0)
                ? Integer.toString((int) value)
                : String.format("%.2f", value);

        return color + icon + " " + capitalize(type.name()) + " " + formatted + (hasPercent ? "%" : "");
    }


}
