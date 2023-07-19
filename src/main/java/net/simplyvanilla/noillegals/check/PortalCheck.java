package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

public class PortalCheck implements Listener {
  private NoIllegalsPlugin plugin;

  public PortalCheck(NoIllegalsPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void handlePortal(EntityPortalEnterEvent event) {
    if (!this.plugin
        .getConfig()
        .getStringList("deniedPortalEntering")
        .contains(event.getEntityType().name())) {
      return;
    }

    event.getEntity().remove();
  }
}
