package net.tinhvv.stats;

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
}
