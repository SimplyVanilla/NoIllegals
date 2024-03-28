package net.simplyvanilla.noillegals.check;

import it.unimi.dsi.fastutil.shorts.ShortReferenceImmutablePair;
import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.Map;

public class BlockLimiter implements Listener {
    private final NoIllegalsPlugin plugin;
    private final Map<Material, Integer> blocks;

    public BlockLimiter(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
        this.blocks = new HashMap<>();
        loadBlocks();
    }

    private void loadBlocks() {
        final FileConfiguration configuration = this.plugin.getConfig();
        final ConfigurationSection limitedBlocks = configuration.getConfigurationSection("limiter.blocks");
        if (limitedBlocks == null) return;

        for (String raw : limitedBlocks.getKeys(false))
            try {
                Material material = Material.valueOf(raw);
                this.blocks.put(material, limitedBlocks.getInt(raw));
            } catch (IllegalArgumentException e) {
                this.plugin.logMaterialNotFound(raw);
            }
    }
}
