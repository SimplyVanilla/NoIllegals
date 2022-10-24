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
        Bukkit.getScheduler().runTaskAsynchronously(NoIllegalsPlugin.getInstance(), () -> {
            if (NoIllegalsPlugin.checkOPPlayers && event.getPlayer().isOp())
                return;

            if (NoIllegalsPlugin.isItemBlocked(event.getBlock().getType()))
                Bukkit.getScheduler().runTaskLater(NoIllegalsPlugin.getInstance(), () -> event.getBlock().setType(Material.AIR), 1L);
        });
    }
}
