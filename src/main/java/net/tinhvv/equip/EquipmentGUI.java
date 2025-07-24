package net.tinhvv.equip;

import net.tinhvv.items.ItemBuilder;
import net.tinhvv.items.Misc.EmptySlotItem;
import net.tinhvv.items.Misc.MissingItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EquipmentGUI {

    public static final String GUI_TITLE = ChatColor.BLACK + "Your Equipment and Stats";

    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(player, 54, GUI_TITLE);

        ItemStack emptySlot = new EmptySlotItem().create();
        ItemStack missingItem = new MissingItem().create();

        ItemStack missingAmulet = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "Missing amulet")
                .build();
        ItemStack missingRing = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "Missing ring")
                .build();
        ItemStack missingGlove = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "Missing glove")
                .build();
        ItemStack missingBelt = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "Missing belt")
                .build();

        ItemStack missingHelmet = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "No Helmet")
                .build();
        ItemStack missingChestplate = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "No Chestplate")
                .build();
        ItemStack missingLeggings = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "No Leggings")
                .build();
        ItemStack missingBoots = new ItemBuilder(new MissingItem().create())
                .name(ChatColor.WHITE + "No Boots")
                .build();

        // Set các ô trống (chặn tương tác)
        setMultipleSlots(gui, emptySlot,
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 12, 13, 14, 15, 16, 17,
                18, 21, 22, 23, 24, 25, 26,
                27, 30, 31, 32, 33, 34, 35,
                36, 39, 40, 41, 42, 43, 44,
                45, 46, 47, 48, 50, 51, 52, 53
        );

        // Nút đóng GUI
        gui.setItem(49, createSlot(Material.BARRIER, ChatColor.WHITE + "Close GUI"));

        // Các ô trang bị (theo đúng vị trí ảnh 1)

        setMultipleSlots(gui, missingAmulet, 10);
        setMultipleSlots(gui, missingRing, 19);
        setMultipleSlots(gui, missingGlove, 28);
        setMultipleSlots(gui, missingBelt, 37);


        // Trang bị
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        gui.setItem(11, helmet != null ? helmet : missingHelmet);
        gui.setItem(20, chestplate != null ? chestplate : missingChestplate);
        gui.setItem(29, leggings != null ? leggings : missingLeggings);
        gui.setItem(38, boots != null ? boots : missingBoots);


        // stats
        gui.setItem(15, createSlot(Material.DIAMOND_SWORD, ChatColor.WHITE + "Combat stats"));
        gui.setItem(16, createSlot(Material.DIAMOND_PICKAXE, ChatColor.WHITE + "Tool"));
        gui.setItem(24, createSlot(Material.BOOK, ChatColor.GOLD + "Skill Book"));
        gui.setItem(25, createSlot(Material.CLOCK, ChatColor.GOLD + "Passive Bonus"));
        gui.setItem(33, createSlot(Material.FISHING_ROD, ChatColor.WHITE + "Utility"));
        gui.setItem(34, createSlot(Material.POTION, ChatColor.WHITE + "Potion"));

        player.openInventory(gui);
    }

    public static ItemStack createSlot(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void setMultipleSlots(Inventory gui, ItemStack item, int... slots) {
        for (int slot : slots) {
            gui.setItem(slot, item);
        }
    }

}
