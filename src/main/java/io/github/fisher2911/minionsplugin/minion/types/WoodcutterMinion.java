package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.task.BlockBreakTask;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class WoodcutterMinion extends BlockMinion {

    // todo
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

    public WoodcutterMinion(final @NotNull JavaPlugin plugin,
                            final @NotNull LocalDateTime lastActionTime,
                            final long id,
                            final @NotNull UUID owner,
                            final @NotNull Region region,
                            final @NotNull MinionData minionData,
                            final @NotNull Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, region, minionData, upgrades);
    }


    @Override
    public boolean performAction(final @NotNull BlockChangedInWorldEvent event) {
        if (!this.isPlaced() ||
                this.isPerformingTask.get()) {
            return true;
        }

        if (!this.canPerformAction()) {
            return false;
        }

        if (event.getType() == BlockChangedInWorldEvent.Type.REMOVED) {
            return true;
        }

        final Block block = event.getBlock();

        final Position position = Position.fromBukkitLocation(block.getLocation());

        if (!allowedMaterials.contains(block.getType())) {
            return true;
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
        return true;
    }
}
