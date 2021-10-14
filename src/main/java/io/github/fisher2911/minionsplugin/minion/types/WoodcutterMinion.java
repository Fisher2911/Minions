package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.task.BlockBreakTask;
import io.github.fisher2911.minionsplugin.world.CuboidRegion;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class WoodcutterMinion extends BaseMinion {

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
                            final long id,
                            final UUID owner,
                            final Position position,
                            final MinionData minionData) {
        super(plugin, id, owner, position, minionData);
    }


    @Override
    public void performAction() {
        if (!this.isPlaced() || this.isPerformingTask.get()) {
            return;
        }

        final ArmorStand minion = this.getMinion();
        final int posX = this.position.getBlockX();
        final int posY = this.position.getBlockY();
        final int posZ = this.position.getBlockZ();

        final int size = 5;

        final int minX = posX - size;
        final int minY = posY - size;
        final int minZ = posZ - size;
        final int maxX = posX + size;
        final int maxY= posY + size;
        final int maxZ = posZ + size;

        final World world = this.minion.getWorld();

        final Region region = new CuboidRegion(
                world,
                minX, posY - 30, minZ,
                maxX, posY + 30, maxZ
                );

        for (int x = posX - size; x < posX + size; x++) {
            for (int y = posY - size; y < posY + size; y++) {
                for (int z = posZ - size; z < posZ + size; z++) {
                    final Position position = new Position(
                            world,
                            x, y, z
                    );

                    final Block block = position.getBlock();

                    if (!allowedMaterials.contains(block.getType())) {
                        continue;
                    }

                    final BlockBreakTask task = new BlockBreakTask(
                        this.plugin,
                            position,
                            region,
                            1,
                            allowedMaterials,
                            brokenBlock -> this.getInventory().addItem(
                                        brokenBlock.getDrops().toArray(new ItemStack[0])),
                            () -> this.isPerformingTask.set(false)
                    );
                    task.start();
                    this.isPerformingTask.set(true);
                    return;
                }
            }
        }
    }
}
