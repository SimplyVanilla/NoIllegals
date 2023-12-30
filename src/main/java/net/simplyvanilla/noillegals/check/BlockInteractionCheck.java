package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;

public class BlockInteractionCheck implements Listener {
  private final NoIllegalsPlugin plugin;

  public BlockInteractionCheck(NoIllegalsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(ignoreCancelled = true)
  public void handlePlayerInteract(PlayerInteractEvent event) {
    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

    Player player = event.getPlayer();

    if (this.plugin.isCheckOPPlayers() && player.isOp()) {
      return;
    }

    Block block = event.getClickedBlock();

    if (!(block.getState() instanceof BlockInventoryHolder)) {
      return;
    }

    this.plugin.logInventoryOpen(
        player,
        block.getType(),
        block.getLocation().getBlockX(),
        block.getLocation().getBlockY(),
        block.getLocation().getBlockZ());
  }
}
