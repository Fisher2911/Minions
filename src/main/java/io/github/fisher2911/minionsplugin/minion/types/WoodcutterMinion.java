package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.task.BlockBreakTask;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class WoodcutterMinion extends BlockMinion {

    private static final Set<Material> allowedMaterials;
    private AtomicBoolean isPerformingTask = new AtomicBoolean(false);

    static {
        allowedMaterials = Arrays.stream(Material.values()).
                filter(material -> {
                    final String string = material.toString();
                    return string.contains("LOG") ||
                            string.contains("LEAVES");
                }).collect(Collectors.toSet());
    }

    public WoodcutterMinion(final JavaPlugin plugin,
                            final LocalDateTime lastActionTime,
                            final long id,
                            final UUID owner,
                            final Region region,
                            final MinionData minionData) {
        super(plugin, lastActionTime, id, owner, region, minionData);
    }


    @Override
    public void performAction(final Block block) {
        if (!this.isPlaced() ||
                this.isPerformingTask.get()) {
            return;
        }

        final Position position = Position.fromBukkitLocation(block.getLocation());

        if (!allowedMaterials.contains(block.getType())) {
            return;
        }

        final BlockBreakTask task = new BlockBreakTask(
                this.plugin,
                position,
                this.region,
                1,
                allowedMaterials,
                brokenBlock -> this.getInventory().addItem(
                        brokenBlock.getDrops().toArray(new ItemStack[0])),
                () -> {
                    this.isPerformingTask.set(false);
                    this.setLastActionTime(LocalDateTime.now());
                }
        );
        task.start();
        this.isPerformingTask.set(true);
    }
}
