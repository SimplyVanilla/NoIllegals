package net.simplyvanilla.noillegals;

import net.simplyvanilla.noillegals.check.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class NoIllegalsPlugin extends JavaPlugin {

    private static NoIllegalsPlugin instance;
    public static boolean checkOPPlayers;
    public static String infoLogText = "";

    private static final List<Material> blockedItems = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (getConfig().getBoolean("check.playerLoginCheck"))
            getServer().getPluginManager().registerEvents(new LoginCheck(), this);

        if (getConfig().getBoolean("check.playerInventoryCloseCheck"))
            getServer().getPluginManager().registerEvents(new InventoryCheck(), this);

        if (getConfig().getBoolean("check.checkItemDrop"))
            getServer().getPluginManager().registerEvents(new ItemDropCheck(), this);

        if (getConfig().getBoolean("check.playerPickupCheck"))
            getServer().getPluginManager().registerEvents(new ItemCollectCheck(), this);

        if (getConfig().getBoolean("check.chestCloseCheck"))
            getServer().getPluginManager().registerEvents(new ChestCheck(), this);

        if (getConfig().getBoolean("check.enderChestCheck"))
            getServer().getPluginManager().registerEvents(new EnderChestCheck(), this);

        if (getConfig().getBoolean("check.shulkerCloseCheck"))
            getServer().getPluginManager().registerEvents(new ShulkerCheck(), this);

        if (getConfig().getBoolean("check.checkBlockPlace"))
            getServer().getPluginManager().registerEvents(new BlockPlaceCheck(), this);

        if (getConfig().getBoolean("check.checkCrafts"))
            getServer().getPluginManager().registerEvents(new CraftCheck(), this);

        checkOPPlayers = getConfig().getBoolean("check.checkOPPlayers");

        if (getConfig().isSet("log.itemRemoved")) {
            infoLogText = getConfig().getString("log.itemRemoved");
        }

        getConfig().getStringList("blockedItemTypes").forEach(text -> {
                    Material material = Material.getMaterial(text);
                    if (material != null)
                        blockedItems.add(material);
                    else
                        getLogger().log(Level.SEVERE, "Material called " + text + " is cannot found!");
                }
        );

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static boolean isItemBlocked(Material material) {
        return blockedItems.contains(material);
    }

    public static NoIllegalsPlugin getInstance() {
        return instance;
    }

    public static void log(Player player, Material material) {
        if (!infoLogText.isEmpty())
            instance.getLogger().log(Level.INFO, infoLogText.replace("[item]", material.name()).replace("[player_name]", player.getName()));
    }
}
