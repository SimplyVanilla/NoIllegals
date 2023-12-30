package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

public class LoginCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public LoginCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (this.plugin.isCheckOPPlayers() && event.getPlayer().isOp()) {
            return;
        }

        Bukkit.getScheduler()
            .runTaskLater(
                this.plugin,
                () -> {
                    for (ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
                        if (itemStack != null && this.plugin.isItemBlocked(itemStack.getType())) {
                            this.plugin.log(event.getPlayer(), itemStack.getType());
                            itemStack.setAmount(0);
                        }
                    }
                },
                1L);
    }
}
