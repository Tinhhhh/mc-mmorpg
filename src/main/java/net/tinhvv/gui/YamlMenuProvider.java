package net.tinhvv.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class YamlMenuProvider implements InventoryProvider {

    private final FileConfiguration config;

    public YamlMenuProvider(String fileName) {
        File file = new File(JavaPlugin.getPlugin(Mmorpg.class).getDataFolder(), "gui/" + fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        int rows = config.getInt("size", 4);

        // Handle fill
        ConfigurationSection fillSection = config.getConfigurationSection("fill");
        if (fillSection != null && fillSection.getBoolean("enabled", false)) {
            String materialName = fillSection.getString("material", "BLACK_STAINED_GLASS_PANE");
            Material fillMaterial = Material.matchMaterial(materialName.toUpperCase());
            if (fillMaterial != null) {
                ItemStack filler = new ItemStack(fillMaterial);
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < 9; col++) {
                        contents.set(SlotPos.of(row, col), ClickableItem.empty(filler));
                    }
                }
            }
        }

        // Load items
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items != null) {
            for (String key : items.getKeys(false)) {
                ConfigurationSection item = items.getConfigurationSection(key);
                int row = item.getInt("pos", 0) / 9;
                int col = item.getInt("pos", 0) % 9;
                Material material = Material.matchMaterial(item.getString("material", "STONE"));
                String name = ChatColor.translateAlternateColorCodes('<', item.getString("display_name", ""));

                ItemStack stack = new ItemStack(material);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(name);
                stack.setItemMeta(meta);

                contents.set(SlotPos.of(row, col), ClickableItem.empty(stack));
            }
        }
    }


    @Override
    public void update(Player player, InventoryContents contents) {
        // Optional: update logic
    }
}
