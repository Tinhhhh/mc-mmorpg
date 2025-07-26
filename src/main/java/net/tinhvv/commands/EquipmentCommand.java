package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.tinhvv.inventoryProvider.gui.EquipmentProvider;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;

@CommandAlias("equippment|eq")
public class EquipmentCommand extends BaseCommand {
    @Default
    public void onCommand(Player player) {
        Mmorpg.getEquipmentManager().getOrCreate(player); // Load trước cho an toàn
        EquipmentProvider.open(player);
    }

}
