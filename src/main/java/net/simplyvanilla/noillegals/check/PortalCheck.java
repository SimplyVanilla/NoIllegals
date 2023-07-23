package net.simplyvanilla.noillegals.check;

import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class PortalCheck implements Listener {
    private final NoIllegalsPlugin plugin;

    public PortalCheck(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handlePortal(EntityPortalEvent event) {
        if (!this.plugin
            .getConfig()
            .getStringList("deniedPortalEntering")
            .contains(event.getEntityType().name())) {
            return;
        }



        event.setCancelled(true);
    }

    @EventHandler
    public void handle(EntityTeleportEndGatewayEvent event) {
        if (!this.plugin
            .getConfig()
            .getStringList("deniedPortalEntering")
            .contains(event.getEntityType().name())) {
            return;
        }

        event.setCancelled(true);
    }
}
