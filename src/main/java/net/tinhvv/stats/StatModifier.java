package net.tinhvv.stats;

import java.util.List;

public class StatModifier {

    private final StatType stat;
    private final double value;
    private final String source; // để track từ đâu (item, buff,...)

    public StatModifier(StatType stat, double value, String source) {
        this.stat = stat;
        this.value = value;
        this.source = source;
    }

    public StatType getStat() { return stat; }
    public double getValue() { return value; }
    public String getSource() { return source; }

    public static StatModifier getModifierByStatType(List<StatModifier> modifiers, StatType type) {
        for (StatModifier mod : modifiers) {
            if (mod.getStat() == type) {
                return mod;
            }
        }
        return null; // hoặc Optional.empty() nếu muốn xài Optional
    }


}
