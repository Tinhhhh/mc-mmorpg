package net.tinhvv.mmorpg;

import co.aikar.commands.BukkitCommandManager;
import fr.minuskube.inv.InventoryManager;
import net.tinhvv.commands.*;
import net.tinhvv.listeners.InventoryListener;
import net.tinhvv.listeners.PlayerJoinListener;
import net.tinhvv.listeners.misc.MiscListener;
import net.tinhvv.manager.CustomItemManager;
import net.tinhvv.manager.EquipmentManager;
import net.tinhvv.manager.StatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mmorpg extends JavaPlugin {

    private static Mmorpg instance;
    private static StatManager statManager;
    private static InventoryManager inventoryManager;
    private static EquipmentManager equipmentManager;
    private static CustomItemManager customItemManager;

    public static Mmorpg getInstance() {
        return instance;
    }

    public static StatManager getStatManager() {
        return statManager;
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static EquipmentManager getEquipmentManager() {
        return equipmentManager;
    }

    public static CustomItemManager getCustomItemManager() {
        return customItemManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Khởi tạo các manager trước
        statManager = new StatManager();
        equipmentManager = new EquipmentManager();
        inventoryManager = new InventoryManager(this);
        customItemManager = new CustomItemManager();

        // Init các hệ thống cần thiết
        inventoryManager.init();
        customItemManager.init();

        // Copy file GUI YAML từ JAR ra thư mục plugin (nếu chưa có)
        saveResource("gui/stats.yml", true);
        saveResource("gui/equipment.yml", true);

        // Load dữ liệu cho tất cả player đang online
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            statManager.load(player);
//            equipmentManager.load(player);
//            Mmorpg.getStatManager().updateFromEquipment(player);
//        }

        // Đăng ký command
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new EasterEggCommand());
        manager.registerCommand(new MenuCommand());
        manager.registerCommand(new StatsCommand());
        manager.registerCommand(new EquipmentCommand());
        manager.registerCommand(new TestItemCommand());

        // Đăng ký listener
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MiscListener(), this);
    }


    @Override
    public void onDisable() {
        // Save dữ liệu cho tất cả player đang online
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            Mmorpg.getStatManager().save(player);
//            Mmorpg.getEquipmentManager().save(player);
//        }

        getLogger().info("MMORPG plugin disabled.");
    }
}
