package net.tinhvv.stats;

import java.util.EnumMap;
import java.util.Map;

/**
 * Chứa giá trị mặc định của tất cả các chỉ số StatType,
 * dùng để khởi tạo khi player mới hoặc khi không có gì được thiết lập.
 */
public class StatDefaults {

    private static final Map<StatType, Double> defaults = new EnumMap<>(StatType.class);

    static {
        defaults.put(StatType.STRENGTH, 0.0);
        defaults.put(StatType.HEALTH, 20.0); // mặc định 10 tim = 20 máu
        defaults.put(StatType.REGENERATION, 0.0);
        defaults.put(StatType.ARMOR, 0.0);
        defaults.put(StatType.TOUGHNESS, 0.0);
        defaults.put(StatType.LUCK, 0.0);
        defaults.put(StatType.WISDOM, 0.0);
        defaults.put(StatType.CRIT_CHANCE, 0.0);
        defaults.put(StatType.CRIT_DAMAGE, 0.0);
        defaults.put(StatType.SPEED, 10.0); // Mặc định là 10% (1.0 movement speed x 100)
    }

    /**
     * Trả về giá trị mặc định của 1 stat type
     */
    public static double get(StatType type) {
        return defaults.getOrDefault(type, 0.0);
    }

    /**
     * Trả về map copy để sử dụng khởi tạo
     */
    public static Map<StatType, Double> getAllDefaults() {
        return new EnumMap<>(defaults);
    }
}
