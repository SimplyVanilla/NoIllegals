package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public BlockPlaceCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Bukkit.getScheduler()
            .runTaskAsynchronously(
                this.plugin,
                () -> {
                    if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) {
                        return;
                    }

                    // We check if the placed item is a end portal, the block that was placed against is a
                    // end portal, and the item in hand is a ender eye.
                    if (this.plugin.isItemBlocked(event.getBlock().getType())
                        && event.getBlock().getType().equals(Material.END_PORTAL_FRAME)
                        && event.getBlockAgainst().getType().equals(Material.END_PORTAL_FRAME)
                        && event.getItemInHand().getType().equals(Material.ENDER_EYE)) {
                        return;
                    }

                    if (this.plugin.isItemBlocked(event.getBlock().getType())) {
                        Bukkit.getScheduler()
                            .runTaskLater(this.plugin, () -> {
                                this.plugin.log(event.getPlayer(), event.getBlock().getType());
                                event.getBlock().setType(Material.AIR);
                            }, 1L);
                    }
                });
    }
}
