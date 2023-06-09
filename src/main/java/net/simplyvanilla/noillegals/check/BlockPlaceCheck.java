package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceCheck implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // We check if the placed item is a end portal, the block that was placed against is a end portal, and the item in hand is a ender eye.
        if (event.getBlock().getType().equals(Material.END_PORTAL_FRAME) &&
            event.getBlockAgainst().getType().equals(
                Material.END_PORTAL_FRAME) &&
            event.getItemInHand().getType().equals(Material.ENDER_EYE)) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(NoIllegalsPlugin.getInstance(), () -> {
            if (NoIllegalsPlugin.checkOPPlayers && event.getPlayer().isOp()) {
                return;
            }

            if (NoIllegalsPlugin.isItemBlocked(event.getBlock().getType())) {
                Bukkit.getScheduler().runTaskLater(NoIllegalsPlugin.getInstance(),
                    () -> event.getBlock().setType(Material.AIR), 1L);
            }
        });
    }
}
