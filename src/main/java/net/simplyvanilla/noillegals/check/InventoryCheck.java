package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public InventoryCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInventoryClose(InventoryCloseEvent event) {
        if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) return;
        for (ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            if (itemStack != null && this.plugin.isItemBlocked(itemStack.getType())) {
                this.plugin.log((Player) event.getPlayer(), itemStack.getType());
                itemStack.setAmount(0);
            }
        }
    }
}
