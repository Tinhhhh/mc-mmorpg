package net.tinhvv.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.tinhvv.items.CustomItem;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// /giveitem <id>
@CommandAlias("giveitem")
public class TestItemCommand extends BaseCommand {

    @Default
    @Syntax("<id> [số lượng]")
    @Description("Nhận item tùy chỉnh từ ID")
    public void onGiveItem(Player player, String id, @Optional @Default("1") int amount) {
        CustomItem item = Mmorpg.getCustomItemManager().getById(id);
        if (item == null) {
            player.sendMessage("§cKhông tìm thấy item có ID: §e" + id);
            return;
        }

        ItemStack stack = item.create();
        stack.setAmount(amount);

        player.getInventory().addItem(stack);
        player.sendMessage("§aĐã nhận §e" + amount + " §ax " + id);
    }
}



