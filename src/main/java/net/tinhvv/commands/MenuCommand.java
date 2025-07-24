package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.tinhvv.equip.EquipmentGUI;
import org.bukkit.entity.Player;
@CommandAlias("menu")
public class MenuCommand extends BaseCommand {

    @Default
    public void onMenu (Player player) {
        EquipmentGUI.open(player);
    }
}
