package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.tinhvv.items.EasterEgg.GoblinEgg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("ee")
public class EasterEggCommand extends BaseCommand {

    @Default
    public void onEasterEgg(Player player) {
        ItemStack item = new GoblinEgg().create();
        player.getInventory().addItem(item);
    }
}
