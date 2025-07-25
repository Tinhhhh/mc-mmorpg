package net.tinhvv.stats;

public class StatFormat {
    public static String format(StatType type, double value) {
        String color, icon;

        switch (type) {
            case STRENGTH -> { color = "§4"; icon = "✕"; }
            case HEALTH -> { color = "§c"; icon = "❤"; }
            case REGENERATION -> { color = "§c"; icon = "\uD83D\uDC9E"; }
            case LUCK -> { color = "§5"; icon = "\uD83C\uDF40"; }
            case WISDOM -> { color = "§b"; icon = "✦"; }
            case TOUGHNESS -> { color = "§2"; icon = "❖"; }
            case CRIT_CHANCE -> { color = "§9"; icon = "\uD83D\uDCAB"; }
            case CRIT_DAMAGE -> { color = "§9"; icon = "☠"; }
            case SPEED -> { color = "§f"; icon = "\uD83D\uDC5F"; }
            default -> { color = "§7"; icon = "-"; }
        }

        String suffix = (type.name().contains("CRIT") || type == StatType.SPEED) ? "%" : "";
        return color + icon + " " + capitalize(type.name()) + " " + value + suffix;
    }

    private static String capitalize(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase().replace("_", " ");
    }
}
