package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.task.BlockBreakTask;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
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

    public WoodcutterMinion(final JavaPlugin plugin,
                            final Instant lastActionTime,
                            final long id,
                            final UUID owner,
                            final MinionType minionType,
                            final Position position,
                            final MinionData minionData,
                            final Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, minionType, position, minionData, upgrades);
    }


    @Override
    public ActionResult performAction(final BlockChangedInWorldEvent event) {
        if (this.isPerformingTask.get()) {
            return ActionResult.FAIL;
        }

        if (event.getType() == BlockChangedInWorldEvent.Type.REMOVED) {
            return ActionResult.NOT_POSSIBLE;
        }

        final Block block = event.getBlock();

        final Position position = Position.fromBukkitLocation(block.getLocation());

        if (!allowedMaterials.contains(block.getType())) {
            return ActionResult.NOT_POSSIBLE;
        }

        final BlockBreakTask task = new BlockBreakTask(
                this.plugin,
                position,
                this.getRegion(),
                1,
                allowedMaterials,
                brokenBlock -> this.getInventory().addStoredItemStack(
                        brokenBlock.getDrops().toArray(new ItemStack[0])),
                () -> {
                    this.isPerformingTask.set(false);
                    this.setLastActionTime(Instant.now());
                }
        );
        task.start();
        this.isPerformingTask.set(true);
        return ActionResult.SUCCESS;
    }
}
