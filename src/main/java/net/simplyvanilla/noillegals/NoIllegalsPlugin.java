package net.simplyvanilla.noillegals;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.simplyvanilla.noillegals.check.BlockInteractionCheck;
import net.simplyvanilla.noillegals.check.BlockPlaceCheck;
import net.simplyvanilla.noillegals.check.CraftCheck;
import net.simplyvanilla.noillegals.check.InventoryItemCheck;
import net.simplyvanilla.noillegals.check.ItemCollectCheck;
import net.simplyvanilla.noillegals.check.ItemDropCheck;
import net.simplyvanilla.noillegals.check.LoginCheck;
import net.simplyvanilla.noillegals.check.PortalCheck;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoIllegalsPlugin extends JavaPlugin {
    private static final String PLAYER_NAME_PLACEHOLDER = "[player_name]";

    private boolean checkOPPlayers;
    private String infoLogText = "";
    private String inventoryOpenLogText = "";
    private String playerItemReceiveLogText = "";

    private final List<Material> blockedItems = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getConfig().getBoolean("check.playerLoginCheck")) {
            getServer().getPluginManager().registerEvents(new LoginCheck(this), this);
        }

        if (getConfig().getBoolean("check.checkItemDrop")) {
            getServer().getPluginManager().registerEvents(new ItemDropCheck(this), this);
        }

        if (getConfig().getBoolean("check.playerPickupCheck")) {
            getServer().getPluginManager().registerEvents(new ItemCollectCheck(this), this);
        }

        if (getConfig().getBoolean("check.inventoryMoveItemCheck")) {
            getServer().getPluginManager().registerEvents(new InventoryItemCheck(this), this);
        }

        if (getConfig().getBoolean("check.checkBlockPlace")) {
            getServer().getPluginManager().registerEvents(new BlockPlaceCheck(this), this);
        }

        if (getConfig().getBoolean("check.checkCrafts")) {
            getServer().getPluginManager().registerEvents(new CraftCheck(this), this);
        }

        if (getConfig().getBoolean("check.checkPortal")) {
            getServer().getPluginManager().registerEvents(new PortalCheck(this), this);
        }

        if (getConfig().getBoolean("check.blockInteractionCheck")) {
            getServer().getPluginManager().registerEvents(new BlockInteractionCheck(this), this);
        }

        checkOPPlayers = getConfig().getBoolean("check.checkOPPlayers");

        if (getConfig().isSet("log.itemRemoved")) {
            infoLogText = getConfig().getString("log.itemRemoved");
        }

        if (getConfig().isSet("log.playerItemReceive")) {
            playerItemReceiveLogText = getConfig().getString("log.playerItemReceive");
        }

        if (getConfig().isSet("log.inventoryOpen")) {
            inventoryOpenLogText = getConfig().getString("log.inventoryOpen");
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
                            .log(Level.SEVERE,
                                () -> "Material called " + text + " is cannot found!");
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
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName()));
        }
    }

    public void logInventoryOpen(Player player, Material material, int x, int y, int z) {
        if (!inventoryOpenLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        inventoryOpenLogText
                            .replace("[block_type]", material.name())
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[x]", String.valueOf(x))
                            .replace("[y]", String.valueOf(y))
                            .replace("[z]", String.valueOf(z)));
        }
    }

    public void logPlayerItemReceive(Player player, Material material, int amount) {
        if (!playerItemReceiveLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        playerItemReceiveLogText
                            .replace("[item]", material.name())
                            .replace("[amount]", String.valueOf(amount))
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[x]", String.valueOf(player.getLocation().getBlockX()))
                            .replace("[y]", String.valueOf(player.getLocation().getBlockY()))
                            .replace("[z]", String.valueOf(player.getLocation().getBlockZ())));
        }
    }

    public boolean isCheckOPPlayers() {
        return checkOPPlayers;
    }

    public String getInfoLogText() {
        return infoLogText;
    }
}
