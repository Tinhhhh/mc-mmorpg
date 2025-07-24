package net.tinhvv.mmorpg;

import co.aikar.commands.BukkitCommandManager;
import net.tinhvv.commands.EasterEggCommand;
import net.tinhvv.commands.EquipCommand;
import net.tinhvv.commands.MenuCommand;
import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.listeners.InventoryListener;
import net.tinhvv.listeners.PlayerJoinListener;
import net.tinhvv.listeners.misc.MiscListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mmorpg extends JavaPlugin {

    private static Mmorpg instance;

    public static Mmorpg getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {

        instance = this;

        //Registering custom items
        CustomItemRegistry.init();

        // Plugin startup logic
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new EasterEggCommand());
        manager.registerCommand(new MenuCommand());
        manager.registerCommand(new EquipCommand());

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
