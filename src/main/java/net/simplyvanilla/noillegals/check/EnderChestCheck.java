package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class EnderChestCheck implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) { // Checks ender chest
        if (event.getInventory().getType() != InventoryType.ENDER_CHEST)
            return;

        else if (NoIllegalsPlugin.checkOPPlayers && event.getPlayer().isOp())
            return;

        for (ItemStack itemStack : event.getView().getTopInventory().getContents()) {
            if (itemStack != null && NoIllegalsPlugin.isItemBlocked(itemStack.getType())) {
                itemStack.setAmount(0);
                NoIllegalsPlugin.log((Player) event.getPlayer(), itemStack.getType());
            }
        }
    }
}
