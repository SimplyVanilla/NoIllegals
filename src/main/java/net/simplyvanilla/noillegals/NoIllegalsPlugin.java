package net.simplyvanilla.noillegals;

import net.simplyvanilla.noillegals.check.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class NoIllegalsPlugin extends JavaPlugin {

  private boolean checkOPPlayers;
  private String infoLogText = "";

  private final List<Material> blockedItems = new ArrayList<>();

  @Override
  public void onEnable() {
    saveDefaultConfig();
    if (getConfig().getBoolean("check.playerLoginCheck")) {
      getServer().getPluginManager().registerEvents(new LoginCheck(this), this);
    }

    if (getConfig().getBoolean("check.playerInventoryCloseCheck")) {
      getServer().getPluginManager().registerEvents(new InventoryCheck(this), this);
    }

    if (getConfig().getBoolean("check.checkItemDrop")) {
      getServer().getPluginManager().registerEvents(new ItemDropCheck(this), this);
    }

    if (getConfig().getBoolean("check.playerPickupCheck")) {
      getServer().getPluginManager().registerEvents(new ItemCollectCheck(this), this);
    }

    if (getConfig().getBoolean("check.chestCloseCheck")) {
      getServer().getPluginManager().registerEvents(new ChestCheck(this), this);
    }

    if (getConfig().getBoolean("check.enderChestCheck")) {
      getServer().getPluginManager().registerEvents(new EnderChestCheck(this), this);
    }

    if (getConfig().getBoolean("check.shulkerCloseCheck")) {
      getServer().getPluginManager().registerEvents(new ShulkerCheck(this), this);
    }

    if (getConfig().getBoolean("check.checkBlockPlace")) {
      getServer().getPluginManager().registerEvents(new BlockPlaceCheck(this), this);
    }

    if (getConfig().getBoolean("check.checkCrafts")) {
      getServer().getPluginManager().registerEvents(new CraftCheck(this), this);
    }

    checkOPPlayers = getConfig().getBoolean("check.checkOPPlayers");

    if (getConfig().isSet("log.itemRemoved")) {
      infoLogText = getConfig().getString("log.itemRemoved");
    }

    getConfig()
        .getStringList("blockedItemTypes")
        .forEach(
            text -> {
              Material material = Material.getMaterial(text);
              if (material != null) {
                blockedItems.add(material);
              } else {
                getLogger()
                    .log(Level.SEVERE, () -> "Material called " + text + " is cannot found!");
              }
            });
  }

  public boolean isItemBlocked(Material material) {
    return blockedItems.contains(material);
  }

  public void log(Player player, Material material) {
    if (!infoLogText.isEmpty()) {
      this.getLogger()
          .log(
              Level.INFO,
              () ->
                  infoLogText
                      .replace("[item]", material.name())
                      .replace("[player_name]", player.getName()));
    }
  }

  public boolean isCheckOPPlayers() {
    return checkOPPlayers;
  }

  public String getInfoLogText() {
    return infoLogText;
  }
}
