package net.simplyvanilla.noillegals.check;

import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    void on(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        if (player.hasPermission(this.plugin.getName() + ".bypass")) return;

        Material type = event.getBlock().getType();
        int max = this.blocks.getOrDefault(type, -1);
        if (max > 0) {
            //TODO: check
            event.setCancelled(true);
        }
    }

    private class ChunkPosition {
        private final int x;
        private final int z;

        public ChunkPosition(Chunk chunk) {
            this.x = chunk.getX();
            this.z = chunk.getZ();
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }
}
