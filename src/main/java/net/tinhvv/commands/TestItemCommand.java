package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.tinhvv.items.CustomItem;
import net.tinhvv.items.CustomItemRegistry;
import org.bukkit.entity.Player;

// /giveitem <id>
@CommandAlias("giveitem")
public class TestItemCommand extends BaseCommand {

    @Default
    @Description("Nhận item tùy chỉnh từ ID")
    public void onGiveItem(Player player, String id) {
        CustomItem item = CustomItemRegistry.getById(id);
        if (item == null) {
            player.sendMessage("§cKhông tìm thấy item có ID: §e" + id);
            return;
        }

        player.getInventory().addItem(item.create());
        player.sendMessage("§aĐã nhận item: §e" + id);
    }
}


