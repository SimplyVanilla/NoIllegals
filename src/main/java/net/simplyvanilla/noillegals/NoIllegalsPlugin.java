package net.simplyvanilla.noillegals;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.simplyvanilla.noillegals.check.BlockInteractionCheck;
import net.simplyvanilla.noillegals.check.BlockPlaceCheck;
import net.simplyvanilla.noillegals.check.CraftCheck;
import net.simplyvanilla.noillegals.check.InventoryCreationCheck;
import net.simplyvanilla.noillegals.check.InventoryItemCheck;
import net.simplyvanilla.noillegals.check.ItemCollectCheck;
import net.simplyvanilla.noillegals.check.ItemDropCheck;
import net.simplyvanilla.noillegals.check.LoginCheck;
import net.simplyvanilla.noillegals.check.PortalCheck;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoIllegalsPlugin extends JavaPlugin {
    private static final String PLAYER_NAME_PLACEHOLDER = "[player_name]";
    private static final String ITEM_PLACEHOLDER = "[item]";
    private static final String AMOUNT_PLACEHOLDER = "[amount]";
    private static final String ENCHANTMENTS_PLACEHOLDER = "[enchantments]";

    private boolean checkOPPlayers;
    private String infoLogText = "";
    private String inventoryOpenLogText = "";
    private String playerItemReceiveLogText = "";
    private String playerItemSentLogText = "";
    private String inventoryCreationLogText = "";
    private String playerDroppedItemLogText = "";

    private final List<Material> blockedItems = new ArrayList<>();
    private final List<Material> loggedItemTypes = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        enableChecks();

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

        if (getConfig().isSet("log.playerItemSent")) {
            playerItemSentLogText = getConfig().getString("log.playerItemSent");
        }

        if (getConfig().isSet("log.inventoryCreation")) {
            inventoryCreationLogText = getConfig().getString("log.inventoryCreation");
        }

        if (getConfig().isSet("log.playerDroppedItem")) {
            playerDroppedItemLogText = getConfig().getString("log.playerDroppedItem");
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

        getConfig()
            .getStringList("loggedItemTypes")
            .forEach(
                text -> {
                    Material material = Material.getMaterial(text);
                    if (material != null) {
                        loggedItemTypes.add(material);
                    } else {
                        getLogger()
                            .log(Level.SEVERE,
                                () -> "Material called " + text + " is cannot found!");
                    }
                });
    }

    private void enableChecks() {
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

        if (getConfig().getBoolean("check.inventoryCreationCheck")) {
            getServer().getPluginManager().registerEvents(new InventoryCreationCheck(this), this);
        }
    }

    public boolean isItemBlocked(Material material) {
        return blockedItems.contains(material);
    }

    public boolean isItemLogged(Material material) {
        return loggedItemTypes.contains(material);
    }

    public void log(Player player, Material material) {
        if (!infoLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        infoLogText
                            .replace(ITEM_PLACEHOLDER, material.name())
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName()));
        }
    }

    public void logInventoryOpen(Player player, String inventoryTitle, int x, int y, int z) {
        if (!inventoryOpenLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        inventoryOpenLogText
                            .replace("[block_type]", inventoryTitle)
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[x]", String.valueOf(x))
                            .replace("[y]", String.valueOf(y))
                            .replace("[z]", String.valueOf(z)));
        }
    }

    public void logPlayerItemReceive(Player player, ItemStack itemStack) {
        // We don't want to log items that are not in the loggedItemTypes list
        if (!this.isItemLogged(itemStack.getType())) {
            return;
        }
        if (!playerItemReceiveLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        playerItemReceiveLogText
                            .replace(ITEM_PLACEHOLDER, itemStack.getType().name())
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(itemStack.getAmount()))
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[x]", String.valueOf(player.getLocation().getBlockX()))
                            .replace("[y]", String.valueOf(player.getLocation().getBlockY()))
                            .replace("[z]", String.valueOf(player.getLocation().getBlockZ()))
                            .replace(ENCHANTMENTS_PLACEHOLDER,
                                itemStack.getEnchantments().entrySet().stream()
                                    .map(entry -> entry.getKey().getKey() + "=" + entry.getValue())
                                    .collect(
                                        Collectors.joining(",")))
                );
        }
    }

    public void logPlayerItemSent(Player player, ItemStack itemStack,
                                  InventoryType inventoryType) {
        // We don't want to log items that are not in the loggedItemTypes list
        if (!this.isItemLogged(itemStack.getType())) {
            return;
        }
        if (!playerItemSentLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        playerItemSentLogText
                            .replace(ITEM_PLACEHOLDER, itemStack.getType().name())
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(itemStack.getAmount()))
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[inventory_type]", inventoryType.name())
                            .replace("[x]", String.valueOf(player.getLocation().getBlockX()))
                            .replace("[y]", String.valueOf(player.getLocation().getBlockY()))
                            .replace("[z]", String.valueOf(player.getLocation().getBlockZ()))
                            .replace(ENCHANTMENTS_PLACEHOLDER,
                                itemStack.getEnchantments().entrySet().stream()
                                    .map(entry -> entry.getKey().getKey() + "=" + entry.getValue())
                                    .collect(
                                        Collectors.joining(","))));
        }
    }

    public void logInventoryCreation(Player player, String inventoryType,
                                     Location location) {
        if (!inventoryCreationLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        inventoryCreationLogText
                            .replace("[inventory_type]", inventoryType)
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[x]", String.valueOf(location.getBlockX()))
                            .replace("[y]", String.valueOf(location.getBlockY()))
                            .replace("[z]", String.valueOf(location.getBlockZ())));
        }
    }

    public void logPlayerDroppedItem(Player player, ItemStack itemStack) {
        // We don't want to log items that are not in the loggedItemTypes list
        if (!this.isItemLogged(itemStack.getType())) {
            return;
        }
        if (!playerDroppedItemLogText.isEmpty()) {
            this.getLogger()
                .log(
                    Level.INFO,
                    () ->
                        playerDroppedItemLogText
                            .replace(ITEM_PLACEHOLDER, itemStack.getType().name())
                            .replace(AMOUNT_PLACEHOLDER, String.valueOf(itemStack.getAmount()))
                            .replace(PLAYER_NAME_PLACEHOLDER, player.getName())
                            .replace("[x]", String.valueOf(player.getLocation().getBlockX()))
                            .replace("[y]", String.valueOf(player.getLocation().getBlockY()))
                            .replace("[z]", String.valueOf(player.getLocation().getBlockZ()))
                            .replace(ENCHANTMENTS_PLACEHOLDER,
                                itemStack.getEnchantments().entrySet().stream()
                                    .map(entry -> entry.getKey().getKey() + "=" + entry.getValue())
                                    .collect(
                                        Collectors.joining(","))));
        }
    }

    public boolean isCheckOPPlayers() {
        return checkOPPlayers;
    }

    public String getInfoLogText() {
        return infoLogText;
    }
}
