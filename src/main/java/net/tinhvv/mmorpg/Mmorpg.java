package net.tinhvv.mmorpg;

import co.aikar.commands.BukkitCommandManager;
import fr.minuskube.inv.InventoryManager;
import net.tinhvv.commands.EasterEggCommand;
import net.tinhvv.commands.EquipCommand;
import net.tinhvv.commands.MenuCommand;
import net.tinhvv.commands.StatsCommand;
import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.listeners.InventoryListener;
import net.tinhvv.listeners.PlayerJoinListener;
import net.tinhvv.listeners.misc.MiscListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mmorpg extends JavaPlugin {

    private static Mmorpg instance;
    private static InventoryManager inventoryManager;

    public static Mmorpg getInstance() {
        return instance;
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }


    @Override
    public void onEnable() {

        instance = this;

        // Copy file GUI YAML từ JAR ra thư mục plugin (nếu chưa có)
        saveResource("gui/stats.yml", true);

        //Registering custom items
        CustomItemRegistry.init();

        // Khởi tạo SmartInventory manager
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Plugin startup logic
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new EasterEggCommand());
        manager.registerCommand(new MenuCommand());
        manager.registerCommand(new EquipCommand());
        manager.registerCommand(new StatsCommand());

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
