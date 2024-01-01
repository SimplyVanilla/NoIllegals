package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.EnderChest;
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
        if (!this.isContainer(event.getBlock())) {
            return;
        }

        // check if player is op and we should ignore them
        if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) {
            return;
        }

        String containerType = this.getContainerType(event.getBlock());

        this.plugin.logInventoryCreation(event.getPlayer(), containerType,
            event.getBlock().getLocation());
    }

    private String getContainerType(Block block) {
        if (block.getState() instanceof EnderChest) {
            return "ENDER_CHEST";
        }

        if (block.getState() instanceof Container container) {
            return container.getInventory().getType().name();
        }

        // should not happen
        return "unknown";
    }

    private boolean isContainer(Block block) {
        if (block.getState() instanceof Container) {
            return true;
        }

        return block.getType().equals(Material.ENDER_CHEST);
    }
}
