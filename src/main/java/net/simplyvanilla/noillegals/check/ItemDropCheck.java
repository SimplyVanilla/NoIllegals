package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropCheck implements Listener {

  @EventHandler
  void onItemDrop(PlayerDropItemEvent event) {
    if (NoIllegalsPlugin.checkOPPlayers && event.getPlayer().isOp()) return;

    if (NoIllegalsPlugin.isItemBlocked(event.getItemDrop().getItemStack().getType())) {
      NoIllegalsPlugin.log(event.getPlayer(), event.getItemDrop().getItemStack().getType());
      event.getItemDrop().remove();
    }
  }
}
