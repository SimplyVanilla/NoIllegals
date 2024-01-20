package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        if (this.plugin.isCheckOPPlayers() && player.isOp()) {
            return;
        }

        Runnable runnable = () -> {
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null && this.plugin.isItemBlocked(itemStack.getType())) {
                    this.plugin.log(player, itemStack.getType());
                    itemStack.setAmount(0);
                }
            }
        };

        if (NoIllegalsPlugin.isFolia()) {
            player.getScheduler().runDelayed(this.plugin, scheduledTask -> runnable.run(), () -> {
            }, 1L);
        } else {
            Bukkit.getScheduler()
                .runTaskLater(
                    this.plugin,
                    runnable,
                    1L);
        }
    }
}
