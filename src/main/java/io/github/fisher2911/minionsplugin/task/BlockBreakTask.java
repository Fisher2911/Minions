package io.github.fisher2911.minionsplugin.task;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class BlockBreakTask implements BlockTask {

    private final JavaPlugin plugin;
    private final Position start;
    private final Region region;
    private final int blocksPerTick;
    private final Set<Material> acceptedBlocks;
    private final Consumer<Block> onBreak;
    private final Runnable onComplete;
    private BukkitTask task;
    private final Queue<Position> blocks;

    public BlockBreakTask(final JavaPlugin plugin,
                          final Position start,
                          final Region region,
                          final int blocksPerTick,
                          final Set<Material> acceptedBlocks,
                          final Consumer<Block> onBreak,
                          final Runnable onComplete) {
        this.plugin = plugin;
        this.start = start;
        this.region = region;
        this.blocksPerTick = blocksPerTick;
        this.acceptedBlocks = acceptedBlocks;
        this.onBreak = onBreak;
        this.onComplete = onComplete;
        this.blocks = this.recursiveCollect(this.start.getBlock(),
                new AtomicInteger(0), 100, new LinkedList<>());
    }

    @Override
    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {

            int totalPlaced = 0;

            if (this.blocks.isEmpty()) {
                this.cancel();
                this.onComplete.run();
                return;
            }

            while (totalPlaced < this.blocksPerTick && !this.blocks.isEmpty()) {
                final Position position = this.blocks.poll();
                final Block block = position.getBlock();
                this.onBreak.accept(block);
                block.setType(Material.AIR);
                totalPlaced++;
            }
        }, 1, 1);
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    private Queue<Position> recursiveCollect(final Block block,
                                          final AtomicInteger total,
                                          int allowedBlocks,
                                          final Queue<Position> collected) {
        final Position position = Position.fromBukkitLocation(
                block.getLocation()
        );

        final Material material = block.getType();
        if (!this.region.contains(position) ||
                !this.acceptedBlocks.contains(material) ||
                total.get() >= allowedBlocks ||
                collected.contains(position)) {
            return collected;
        }

        collected.add(position);

        total.addAndGet(1);

        final Block up = block.getRelative(BlockFace.UP);
        this.recursiveCollect(up, total, allowedBlocks, collected);

        final Block down = block.getRelative(BlockFace.DOWN);
        this.recursiveCollect(down, total, allowedBlocks, collected);

        final Block north = block.getRelative(BlockFace.NORTH);
        this.recursiveCollect(north, total, allowedBlocks, collected);

        final Block south = block.getRelative(BlockFace.SOUTH);
        this.recursiveCollect(south, total, allowedBlocks, collected);

        final Block east = block.getRelative(BlockFace.EAST);
        this.recursiveCollect(east, total, allowedBlocks, collected);

        final Block west = block.getRelative(BlockFace.WEST);
        this.recursiveCollect(west, total, allowedBlocks, collected);

        return collected;
    }
}
