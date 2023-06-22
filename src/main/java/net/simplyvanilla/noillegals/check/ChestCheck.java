package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ChestCheck implements Listener {
  private final NoIllegalsPlugin plugin;

  public ChestCheck(NoIllegalsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void checkChest(InventoryCloseEvent event) {
    if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) return;

    if (event.getInventory().getType() == InventoryType.CHEST) {
      for (ItemStack itemStack : event.getView().getTopInventory().getContents()) {
        if (itemStack != null && this.plugin.isItemBlocked(itemStack.getType())) {
          this.plugin.log((Player) event.getPlayer(), itemStack.getType());
          itemStack.setAmount(0);
        }
      }
    }
  }
}
