package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.tinhvv.inventoryProvider.gui.StatsProvider;
import org.bukkit.entity.Player;

@CommandAlias("stats")
public class StatsCommand extends BaseCommand {

    @Default
    public void onStats(Player player) {
        StatsProvider.open(player);
    }
}

