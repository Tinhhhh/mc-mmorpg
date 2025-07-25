package net.tinhvv.mmorpg;

import co.aikar.commands.BukkitCommandManager;
import fr.minuskube.inv.InventoryManager;
import net.tinhvv.commands.EasterEggCommand;
import net.tinhvv.commands.EquipmentCommand;
import net.tinhvv.commands.MenuCommand;
import net.tinhvv.commands.StatsCommand;
import net.tinhvv.equip.EquipmentManager;
import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.listeners.InventoryListener;
import net.tinhvv.listeners.PlayerJoinListener;
import net.tinhvv.listeners.misc.MiscListener;
import net.tinhvv.stats.StatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mmorpg extends JavaPlugin {

    private static Mmorpg instance;
    private static StatManager statManager;
    private static InventoryManager inventoryManager;
    private static EquipmentManager equipmentManager;

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

    @Override
    public void onEnable() {

        instance = this;

        // Copy file GUI YAML từ JAR ra thư mục plugin (nếu chưa có)
        saveResource("gui/stats.yml", true);
        saveResource("gui/equipment.yml", true);

        //Registering custom items
        inventoryManager = new InventoryManager(this);
        CustomItemRegistry.init();

        // Khởi tạo SmartInventory manager
        inventoryManager.init();

        // Khởi tạo StatManager
        statManager = new StatManager();

        // Khởi tạo EquipmentManager
        equipmentManager = new EquipmentManager();

        // Plugin startup logic
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new EasterEggCommand());
        manager.registerCommand(new MenuCommand());
        manager.registerCommand(new StatsCommand());
        manager.registerCommand(new EquipmentCommand());

        // Registering listeners
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MiscListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Mmorpg plugin has been disabled!");
    }
}
