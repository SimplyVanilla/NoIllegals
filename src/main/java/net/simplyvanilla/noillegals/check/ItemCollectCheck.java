package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemCollectCheck implements Listener {
  private final NoIllegalsPlugin plugin;

  public ItemCollectCheck(NoIllegalsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onItemCollect(EntityPickupItemEvent event) {
    if (this.plugin.isCheckOPPlayers() && event.getEntity().isOp()) return;
    if (this.plugin.isItemBlocked(event.getItem().getItemStack().getType())) {
      this.plugin.log((Player) event.getEntity(), event.getItem().getItemStack().getType());
      event.setCancelled(true);
      event.getItem().remove();
    }
  }
}
