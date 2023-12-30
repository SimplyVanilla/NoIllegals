package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
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
        this.handleItemPickups(player, event);

        // handles when the player shift clicks an item, or swaps items with number keys or offhand
        this.handleItemManipulation(event, player);
    }

    private void handleItemManipulation(InventoryClickEvent event, Player player) {
        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
            || event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)
            || event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
            var item = event.getCurrentItem();

            if (item != null && this.plugin.isItemBlocked(item.getType())) {
                item.setAmount(0);
            }

            if (item != null && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
                && !(event.getClickedInventory() instanceof PlayerInventory)) {
                this.plugin.logPlayerItemReceive(player, item.getType(), item.getAmount());
            }
        }
    }

    private void handleItemPickups(Player player, InventoryClickEvent event) {
        if (event.getAction().name().startsWith("PLACE_")) {
            var item = event.getCursor();

            if (this.plugin.isItemBlocked(item.getType())) {
                item.setAmount(0);
            }

            if (event.getClickedInventory() instanceof PlayerInventory) {
                this.plugin.logPlayerItemReceive(player, item.getType(), item.getAmount());
            }
        }
    }
}
