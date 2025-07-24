package net.tinhvv.listeners.Click;

import net.tinhvv.equip.EquipmentGUI;
import net.tinhvv.equip.EquipmentSlot;
import net.tinhvv.listeners.EventRouter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

import static net.tinhvv.equip.EquipmentGUI.createSlot;

public class EquipmentClickRouter implements EventRouter<InventoryClickEvent> {
    private static final String TITLE = ChatColor.BLACK + "Your Equipment and Stats";

    @Override
    public boolean accept(InventoryClickEvent event) {
        return event.getView().getTitle().equals(TITLE);
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true); // Chặn toàn bộ tương tác mặc định

        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(EquipmentGUI.GUI_TITLE)) return;

        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        // Chỉ xử lý nếu là click trái hoặc phải
        ClickType click = event.getClick();
        if (!(click == ClickType.LEFT || click == ClickType.RIGHT)) return;

        Material type = current.getType();

        if (isHelmet(type)) {
            if (player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType().isAir()) {
                equipArmor(player, current, EquipmentSlot.HELMET);
                event.setCurrentItem(null); // Xoá item trong GUI nếu muốn
            }
        } else if (isChestplate(type)) {
            if (player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType().isAir()) {
                equipArmor(player, current, EquipmentSlot.CHESTPLATE);
                event.setCurrentItem(null);
            }
        } else if (isLeggings(type)) {
            if (player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType().isAir()) {
                equipArmor(player, current, EquipmentSlot.LEGGINGS);
                event.setCurrentItem(null);
            }
        } else if (isBoots(type)) {
            if (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType().isAir()) {
                equipArmor(player, current, EquipmentSlot.BOOTS);
                event.setCurrentItem(null);
            }
        }

        int slot = event.getRawSlot(); // slot trong GUI

        switch (slot) {
            case 11 -> handleArmorUnequip(player, event.getInventory(), slot, EquipmentSlot.HELMET, "No Helmet");
            case 20 -> handleArmorUnequip(player, event.getInventory(), slot, EquipmentSlot.CHESTPLATE, "No Chestplate");
            case 29 -> handleArmorUnequip(player, event.getInventory(), slot, EquipmentSlot.LEGGINGS, "No Leggings");
            case 38 -> handleArmorUnequip(player, event.getInventory(), slot, EquipmentSlot.BOOTS, "No Boots");
        }

        // Cập nhật lại GUI nếu là trang bị
        player.performCommand("menu"); // Mở lại GUI để cập nhật trang bị
    }

    private boolean isArmor(Material type) {
        String name = type.name();
        return name.endsWith("_HELMET")
                || name.endsWith("_CHESTPLATE")
                || name.endsWith("_LEGGINGS")
                || name.endsWith("_BOOTS");
    }


    private boolean isHelmet(Material type) {
        return type.name().endsWith("_HELMET");
    }

    private boolean isChestplate(Material type) {
        return type.name().endsWith("_CHESTPLATE");
    }

    private boolean isLeggings(Material type) {
        return type.name().endsWith("_LEGGINGS");
    }

    private boolean isBoots(Material type) {
        return type.name().endsWith("_BOOTS");
    }

    private void equipArmor(Player player, ItemStack newItem, EquipmentSlot slot) {
        PlayerInventory inv = player.getInventory();

        switch (slot) {
            case HELMET -> inv.setHelmet(newItem.clone());
            case CHESTPLATE -> inv.setChestplate(newItem.clone());
            case LEGGINGS -> inv.setLeggings(newItem.clone());
            case BOOTS -> inv.setBoots(newItem.clone());
        }
    }

    private void safeAddToInventory(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        leftovers.values().forEach(i -> player.getWorld().dropItemNaturally(player.getLocation(), i));
    }

    private void handleArmorUnequip(Player player, Inventory gui, int guiSlot, EquipmentSlot armorSlot, String emptyName) {
        PlayerInventory inv = player.getInventory();
        ItemStack equipped = switch (armorSlot) {
            case HELMET -> inv.getHelmet();
            case CHESTPLATE -> inv.getChestplate();
            case LEGGINGS -> inv.getLeggings();
            case BOOTS -> inv.getBoots();
            default -> null;
        };

        if (equipped != null && !equipped.getType().isAir()) {
            safeAddToInventory(player, equipped);

            // Gỡ trang bị
            switch (armorSlot) {
                case HELMET -> inv.setHelmet(null);
                case CHESTPLATE -> inv.setChestplate(null);
                case LEGGINGS -> inv.setLeggings(null);
                case BOOTS -> inv.setBoots(null);
            }

            // Cập nhật GUI
            EquipmentGUI.setMultipleSlots(gui, createSlot(Material.WHITE_STAINED_GLASS_PANE, ChatColor.GRAY + emptyName), guiSlot);
        }
    }
}
