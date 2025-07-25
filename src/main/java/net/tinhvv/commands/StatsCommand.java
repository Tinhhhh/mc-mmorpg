package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import fr.minuskube.inv.SmartInventory;
import net.tinhvv.gui.StatsProvider;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;

@CommandAlias("stats")
public class StatsCommand extends BaseCommand {

    @Default
    public void onStats(Player player) {
        SmartInventory.builder()
                .id("stats")
                .provider(new StatsProvider("equipment.yml"))
                .size(6, 9)
                .title("Stats")
                .manager(Mmorpg.getInventoryManager())
                .build()
                .open(player);
    }
}

