package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.PlayerInventory;

public class InventoryItemCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public InventoryItemCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleInventoryMoveItem(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) ||
            (this.plugin.isCheckOPPlayers() && player.isOp())) {
            return;
        }
        if (this.plugin.isCheckOPPlayers() && player.isOp()) {
            return;
        }
        // handles when the player picks up an item from the inventory
        if (event.getAction().name().startsWith("PLACE_")) {
            this.handleItemPickups(player, event);
        }

        // handles when the player shift clicks an item, or swaps items with number keys or offhand
        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
            || event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)
            || event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
            this.handleItemManipulation(event, player);
        }
    }

    private void handleItemManipulation(InventoryClickEvent event, Player player) {
        var item = event.getCurrentItem();

        if (item != null && this.plugin.isItemBlocked(item.getType())) {
            this.plugin.log(player, item.getType());
            item.setAmount(0);
            return;
        }

        if (event.getView().getTopInventory().getType().equals(InventoryType.CRAFTING)) {
            return;
        }

        if (item != null && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            if (!(event.getClickedInventory() instanceof PlayerInventory)) {
                this.plugin.logPlayerItemReceive(player, item);
            } else {
                this.plugin.logPlayerItemSent(player, item,
                    event.getView().getTopInventory().getType());
            }
        }
        if ((event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)
            || event.getAction().equals(InventoryAction.HOTBAR_SWAP)) &&
            !item.getType().isAir()) {
            if (!(event.getClickedInventory() instanceof PlayerInventory)) {
                this.plugin.logPlayerItemReceive(player, item);
            } else {
                this.plugin.logPlayerItemSent(player, item,
                    event.getClickedInventory()
                        .getType());
            }
        }

    }

    private void handleItemPickups(Player player, InventoryClickEvent event) {
        var item = event.getCursor();

        if (this.plugin.isItemBlocked(item.getType())) {
            this.plugin.log(player, item.getType());
            item.setAmount(0);
            return;
        }

        if (event.getView().getTopInventory().getType().equals(InventoryType.CRAFTING)) {
            return;
        }
        if (event.getClickedInventory() instanceof PlayerInventory) {
            this.plugin.logPlayerItemReceive(player, item);
        } else {
            this.plugin.logPlayerItemSent(player, item,
                event.getClickedInventory()
                    .getType());
        }
    }
}
