package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemCollectCheck implements Listener {

    @EventHandler
    public void onItemCollect(EntityPickupItemEvent event) {
        if (NoIllegalsPlugin.checkOPPlayers && event.getEntity().isOp())
            return;
        if (NoIllegalsPlugin.isItemBlocked(event.getItem().getItemStack().getType())) {
            NoIllegalsPlugin.log((Player) event.getEntity(), event.getItem().getItemStack().getType());
            event.setCancelled(true);
            event.getItem().remove();
        }
    }
}
