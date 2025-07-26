package net.tinhvv.inventoryProvider.storage;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.tinhvv.equip.EquipmentManager;
import net.tinhvv.equip.EquipmentType;
import net.tinhvv.equip.PlayerEquipment;
import net.tinhvv.items.misc.MissingItem;
import net.tinhvv.mmorpg.Mmorpg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EquipmentInventory implements InventoryProvider {

    private static final Map<EquipmentType, Integer> slotMapping = Map.of(
            EquipmentType.AMULET, 0,
            EquipmentType.RING, 1,
            EquipmentType.GLOVES, 2,
            EquipmentType.BELT, 3
    );

    @Override
    public void init(Player player, InventoryContents contents) {
        PlayerEquipment data = EquipmentManager.get(player);

        for (Map.Entry<EquipmentType, Integer> entry : slotMapping.entrySet()) {
            EquipmentType type = entry.getKey();
            int slot = entry.getValue();
            ItemStack item = data.getItem(type);

            if (item == null || item.getType().isAir()) {
                item = new MissingItem().create();
            }

            ItemStack finalItem = item;
            contents.set(0, slot, ClickableItem.of(item, e -> {
                ItemStack cursor = player.getItemOnCursor();

                // Cập nhật equipment map
                data.setItem(type, cursor.clone());
                player.setItemOnCursor(null);

                // Save to file
                EquipmentManager.save(player);

                // Reload GUI
                open(player);
            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Optional live updates
    }

    public static void open(Player player) {
        SmartInventory.builder()
                .id("equipment_inventory")
                .provider(new EquipmentInventory())
                .size(1, 9)
                .title("Your Equipment")
                .manager(Mmorpg.getInventoryManager())
                .build().open(player);
    }
}
