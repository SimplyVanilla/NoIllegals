package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class InventoryCreationCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public InventoryCreationCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void handleInventoryCreation(BlockPlaceEvent event) {
        // block isn't a container, so we don't care
        if (!(event.getBlock().getState() instanceof Container container)) {
            return;
        }

        // check if player is op and we should ignore them
        if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) {
            return;
        }

        this.plugin.logInventoryCreation(event.getPlayer(), container.getInventory().getType(),
            event.getBlock().getLocation());
    }
}
