package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public CraftCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (this.plugin.isCheckOPPlayers() && event.getWhoClicked().isOp()) {
            return;
        }

        for (ItemStack itemStack : event.getInventory().getContents()) {
            // Checks if item is null
            if (itemStack == null) {
                continue;
            }
            if (this.plugin.isItemBlocked(itemStack.getType())) {
                this.plugin.log((Player) event.getWhoClicked(), itemStack.getType());
                itemStack.setAmount(0);
                event.setCurrentItem(new ItemStack(Material.AIR));
                event.setCancelled(true);
            }
        }
    }
}
