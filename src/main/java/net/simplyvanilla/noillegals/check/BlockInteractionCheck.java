package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.PlayerInventory;

public class BlockInteractionCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public BlockInteractionCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void handlePlayerInteract(InventoryOpenEvent event) {
        if (event.getInventory() instanceof PlayerInventory) {
            return;
        }

        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (this.plugin.isCheckOPPlayers() && player.isOp()) {
            return;
        }

        int x = 0;
        int y = 0;
        int z = 0;
        if (event.getInventory().getLocation() != null) {
            x = event.getInventory().getLocation().getBlockX();
            y = event.getInventory().getLocation().getBlockY();
            z = event.getInventory().getLocation().getBlockZ();
        }

        this.plugin.logInventoryOpen(player, event.getView().getOriginalTitle(), x, y, z);
    }
}
