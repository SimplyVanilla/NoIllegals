package net.simplyvanilla.noillegals.check;

import net.kyori.adventure.text.Component;
import net.simplyvanilla.noillegals.NoIllegalsPlugin;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockLimiter implements Listener {
    private final NoIllegalsPlugin plugin;
    private final Map<Material, Integer> blocks;
    private final List<ChunkPosition> chunks;

    public BlockLimiter(NoIllegalsPlugin plugin) {
        this.plugin = plugin;
        this.blocks = new HashMap<>();
        this.chunks = new ArrayList<>();
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

        Block block = event.getBlock();
        Material type = block.getType();
        int max = this.blocks.getOrDefault(type, -1);
        if (max > 0) {
            ChunkPosition chunkPosition = getChunkPosition(block);
            if (chunkPosition.getPlacedBlocks(type) >= max) {
                String message = this.plugin.getConfig().getString("limiter.message");
                if (message != null && !message.trim().isEmpty())
                    player.sendMessage(Component.text(message
                        .replace("[count]", String.valueOf(max))
                        .replace("[material]", type.name().toLowerCase().replace("_", " "))
                    ));
                event.setCancelled(true);
            } else chunkPosition.placeBlock(type);
        }
    }

    @EventHandler
    void on(BlockBreakEvent event) {
        Block block = event.getBlock();
        getChunkPosition(block).breakBlock(block.getType());
    }

    @EventHandler
    void on(BlockExplodeEvent event) {
        event.blockList().forEach(b -> getChunkPosition(b).breakBlock(b.getType()));
    }

    @EventHandler
    void on(EntityExplodeEvent event) {
        event.blockList().forEach(b -> getChunkPosition(b).breakBlock(b.getType()));
    }

    private ChunkPosition getChunkPosition(Block block) {
        Chunk chunk = block.getChunk();
        return this.chunks.stream().filter(c -> c.getX() == chunk.getX() && c.getZ() == chunk.getZ()).findFirst().orElseGet(() -> {
            ChunkPosition position = new ChunkPosition(chunk);
            this.chunks.add(position);
            return position;
        });
    }

    private static class ChunkPosition {
        private final int x;
        private final int z;
        private final Map<Material, Integer> blocks;

        public ChunkPosition(Chunk chunk) {
            this.x = chunk.getX();
            this.z = chunk.getZ();
            this.blocks = new HashMap<>();
            WorldInfo worldInfo = chunk.getWorld();
            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++)
                    for (int k = worldInfo.getMinHeight(); k < worldInfo.getMaxHeight(); k++)
                        placeBlock(chunk.getBlock(i, k, j).getType());
        }

        public int getPlacedBlocks(Material material) {
            return this.blocks.getOrDefault(material, 0);
        }

        public void placeBlock(Material material) {
            this.blocks.put(material, getPlacedBlocks(material) + 1);
        }

        public void breakBlock(Material material) {
            this.blocks.put(material, Math.max(0, getPlacedBlocks(material) - 1));
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }
}
