package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ChestCheck implements Listener {

    @EventHandler
    public void checkChest(InventoryCloseEvent event) {
        if (NoIllegalsPlugin.checkOPPlayers && event.getPlayer().isOp())
            return;

        if (event.getInventory().getType() == InventoryType.CHEST) {
            for (ItemStack itemStack : event.getView().getTopInventory().getContents()) {
                if (itemStack != null && NoIllegalsPlugin.isItemBlocked(itemStack.getType())) {
                    NoIllegalsPlugin.log((Player) event.getPlayer(), itemStack.getType());
                    itemStack.setAmount(0);
                }
            }
        }
    }
}
