package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

public class LoginCheck implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (NoIllegalsPlugin.checkOPPlayers && event.getPlayer().isOp())
            return;
        Bukkit.getScheduler().runTaskLater(NoIllegalsPlugin.getInstance(), () -> {
            for (ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
                if (itemStack != null && NoIllegalsPlugin.isItemBlocked(itemStack.getType())) {
                    itemStack.setAmount(0);
                    NoIllegalsPlugin.log(event.getPlayer(), itemStack.getType());
                }
            }
        }, 1L);
    }

}
