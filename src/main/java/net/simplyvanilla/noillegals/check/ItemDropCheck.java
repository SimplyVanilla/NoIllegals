package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropCheck implements Listener {
  private final NoIllegalsPlugin plugin;

  public ItemDropCheck(NoIllegalsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  void onItemDrop(PlayerDropItemEvent event) {
    if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) return;

    if (this.plugin.isItemBlocked(event.getItemDrop().getItemStack().getType())) {
      this.plugin.log(event.getPlayer(), event.getItemDrop().getItemStack().getType());
      event.getItemDrop().remove();
    }
  }
}
