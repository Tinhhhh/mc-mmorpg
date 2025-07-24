package net.tinhvv.items.easterEgg;

import net.tinhvv.items.AbstractCustomItem;
import net.tinhvv.items.CustomItemRegistry;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GoblinEgg extends AbstractCustomItem {

    public GoblinEgg() {
        super("goblin_egg", Material.CREEPER_SPAWN_EGG, "easter_egg");
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GREEN + "" + ChatColor.BOLD + "Goblin Egg";
    }

    @Override
    protected List<String> getLore() {
        return Arrays.asList(
                "What is this?",
                "A goblin egg?",
                ChatColor.RED + "Ewwwww~",
                "I should not touch it!"
        );
    }

    @Override
    public void onInteract(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
        crackEgg(player);
        // Trì hoãn 1 tick rồi xóa item trên tay
        Bukkit.getScheduler().runTaskLater(Mmorpg.getInstance(), () -> {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (CustomItemRegistry.match(handItem).isPresent()) {
                player.getInventory().setItemInMainHand(null);
            }
        }, 1L);
    }


    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        crackEgg(player);
        // Trì hoãn 1 tick rồi xóa item trong kho đồ
        Bukkit.getScheduler().runTaskLater(Mmorpg.getInstance(), () -> {
            event.getClickedInventory().setItem(event.getSlot(), null);
        }, 1L);

    }

    private void crackEgg(Player player) {
        player.sendMessage(ChatColor.GREEN + "You cracked the Goblin Egg!");
        player.sendMessage(ChatColor.BOLD + "The egg cracked, and black, foul-smelling fluid spilled from your palm.");
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "I TOLD YOU NOT TO TOUCH IT!!!!");
    }

}
