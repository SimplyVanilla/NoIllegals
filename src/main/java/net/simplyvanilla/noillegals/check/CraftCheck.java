package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftCheck implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (NoIllegalsPlugin.checkOPPlayers && event.getWhoClicked().isOp())
            return;

        for (ItemStack itemStack : event.getInventory().getContents()) {
            if (NoIllegalsPlugin.isItemBlocked(itemStack.getType())) {
                itemStack.setAmount(0);
                event.setCurrentItem(new ItemStack(Material.AIR));
                event.setCancelled(true);
                NoIllegalsPlugin.log((Player) event.getWhoClicked(), itemStack.getType());

            }
        }
    }
}
