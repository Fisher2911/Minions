package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class FarmerMinion extends BlockMinion implements Scheduleable {

    private final Material cropType;
    private final Queue<Position> dirtPositions = new LinkedList<>();
    private final LinkedList<Position> farmlandPositions = new LinkedList<>();

    public FarmerMinion(final JavaPlugin plugin,
                        final LocalDateTime lastActionTime,
                        final long id,
                        final UUID owner,
                        final MinionType minionType,
                        final Position position,
                        final MinionData minionData,
                        final Material cropType,
                        final Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, minionType, position, minionData, upgrades);
        this.cropType = cropType;
        this.checkDirtPositions();
    }

    private void checkDirtPositions() {
        final int y = (int) this.position.getY();
        for (Position position : this.getRegion().getAllPositionsInY(
                y, y
        )) {
            Bukkit.broadcastMessage("Location is " + position.toBukkitLocation().toString());
            final Block block = position.getBlock();
            switch (block.getType()) {
                case DIRT -> this.dirtPositions.add(position);
                case FARMLAND -> this.farmlandPositions.add(position);
            }
        }
    }

    @Override
    public void run() {
        if (!this.canPerformAction()) {
            return;
        }

        final Position dirt = this.dirtPositions.poll();

        if (dirt == null) {
            final Position farmland = this.farmlandPositions.peek();

            if (farmland == null) {
                return;
            }

            final Block block = farmland.getBlock();
            if (!this.performAction(new BlockChangedInWorldEvent(
                    block, BlockChangedInWorldEvent.Type.ADDED
            ))) {
             return;
            }
            this.farmlandPositions.add(this.farmlandPositions.poll());
            return;
        }

        final Block block = dirt.getBlock();

        if (block.getType() != Material.DIRT) {
            if (this.dirtPositions.peek() != null) {
                this.run();
            }
            return;
        }

        block.setType(Material.FARMLAND);
        this.farmlandPositions.addFirst(dirt);
        this.setLastActionTime(LocalDateTime.now());
    }

    @Override
    public boolean performAction(final BlockChangedInWorldEvent event) {
        final Block block = event.getBlock();
        final Position position = Position.fromBukkitLocation(block.getLocation());

        if (!this.getRegion().contains(position)) {
            return false;
        }

        if (event.getType() == BlockChangedInWorldEvent.Type.REMOVED) {
            switch (block.getType()) {
                case DIRT -> this.dirtPositions.remove(position);
                case FARMLAND -> this.farmlandPositions.remove(position);
            }
            return true;
        }

        if (block.getType() != Material.FARMLAND) {
            if (block.getType() != Material.DIRT) {
                return true;
            }

            this.dirtPositions.add(position);
            this.run();
            return true;
        }

        if (!this.canPerformAction()) {
            return false;
        }

        final Block above = block.getRelative(BlockFace.UP);

        if (above.getType().isAir()) {
            above.setType(this.cropType);
            this.setLastActionTime(LocalDateTime.now());
            return true;
        }

        final BlockData blockData = above.getBlockData();

        if (blockData instanceof final Ageable ageable) {
            final int maxAge = ageable.getMaximumAge();
            final int age = ageable.getAge();
            if (age == maxAge) {
                return true;
            }

            ageable.setAge(age + 1);
            above.setBlockData(ageable);
            this.setLastActionTime(LocalDateTime.now());
        }
        return true;
    }
}
