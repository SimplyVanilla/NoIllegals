package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class EnderChestCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public EnderChestCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Checks ender chest
        if (event.getInventory().getType() != InventoryType.ENDER_CHEST) return;

        if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) return;

        for (ItemStack itemStack : event.getView().getTopInventory().getContents()) {
            if (itemStack != null && this.plugin.isItemBlocked(itemStack.getType())) {
                this.plugin.log((Player) event.getPlayer(), itemStack.getType());
                itemStack.setAmount(0);
            }
        }
    }
}
