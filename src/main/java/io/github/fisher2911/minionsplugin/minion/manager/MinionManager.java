package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.minion.types.BlockMinion;
import io.github.fisher2911.minionsplugin.minion.types.EntityMinion;
import io.github.fisher2911.minionsplugin.minion.types.Scheduleable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionManager {

    private final MinionsPlugin plugin;

    // Map of id, BaseMinion<?>
    private final Map<Long, BaseMinion<?>> allMinions = new HashMap<>();

    private final MinionWorldPositions<BlockMinion> blockMinionsWorldPositions = new MinionWorldPositions<>();
    private final MinionWorldPositions<EntityMinion> entityMinionsWorldPositions = new MinionWorldPositions<>();

    private final Map<Long, Scheduleable> schedulerMinionMap = new HashMap<>();

    private BukkitTask schedulerMinionTask;

    public MinionManager(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.startSchedulerMinionTask();
    }

    public void addBlockMinion(final BlockMinion blockMinion) {
        this.addMinion(blockMinion, this.blockMinionsWorldPositions);
    }

    public void addEntityMinion(final EntityMinion entityMinion) {
        this.addMinion(entityMinion, this.entityMinionsWorldPositions);
    }

    public Optional<BlockMinion> getBlockMinionAt(final Position position) {
        return this.getMinionAt(position, this.blockMinionsWorldPositions);
    }

    public Optional<EntityMinion> getEntityMinionAt(final Position position) {
        return this.getMinionAt(position, this.entityMinionsWorldPositions);
    }

    public Optional<MinionPositions<EntityMinion>> getEntityMinionsInChunk(final World world,
                                                                           final long chunkKey) {
        return this.getMinionsInChunk(world, chunkKey, this.entityMinionsWorldPositions);
    }

    public Optional<MinionPositions<BlockMinion>> getBlockMinionsInChunk(final World world,
                                                                         final long chunkKey) {
        return this.getMinionsInChunk(world, chunkKey, this.blockMinionsWorldPositions);
    }

    public Optional<BlockMinion> removeBlockMinion(final BlockMinion blockMinion) {
        return this.removeMinion(blockMinion, this.blockMinionsWorldPositions);
    }

    public Optional<EntityMinion> removeEntityMinion(final EntityMinion entityMinion) {
        return this.removeMinion(entityMinion, this.entityMinionsWorldPositions);
    }

    public Optional<BaseMinion<?>> getBaseMinion(final long id) {
        return Optional.ofNullable(this.allMinions.get(id));
    }

    public void removeMinionsInChunk(final long chunkKey) {
        // todo
    }

    private <T extends BaseMinion<?>> Optional<MinionChunkPositions<T>> getMinionChunksPosition(final Position position,
                                                                                                final MinionWorldPositions<T> minionWorldPositions) {
        final Optional<World> worldOptional = position.getWorld();

        if (worldOptional.isEmpty()) {
            return Optional.empty();
        }

        return minionWorldPositions.get(worldOptional.get());
    }

    private <T extends BaseMinion<?>> void addMinion(final T minion,
                                                     final MinionWorldPositions<T> worldPositions) {
        worldPositions.set(minion);
        this.allMinions.put(minion.getId(), minion);
        if (minion instanceof final Scheduleable scheduleable) {
            this.schedulerMinionMap.put(minion.getId(), scheduleable);
        }
    }

    private <T extends BaseMinion<?>> Optional<T> getMinionAt(final Position position,
                                                              final MinionWorldPositions<T> minionWorldPositions) {
        final Optional<MinionChunkPositions<T>> chunkPositionsOptional =
                this.getMinionChunksPosition(position, minionWorldPositions);

        if (chunkPositionsOptional.isEmpty()) {
            return Optional.empty();
        }

        return chunkPositionsOptional.get().getMinionAt(position);
    }

    public <T extends BaseMinion<?>> Optional<MinionPositions<T>> getMinionsInChunk(final World world,
                                                                                    final long chunkKey,
                                                                                    final MinionWorldPositions<T> minionWorldPositions) {

        final Optional<MinionChunkPositions<T>> optionalMinionChunkPositions =
                minionWorldPositions.get(world);

        if (optionalMinionChunkPositions.isEmpty()) {
            return Optional.empty();
        }

        return optionalMinionChunkPositions.get().get(chunkKey);
    }

    private <T extends BaseMinion<?>> Optional<T> removeMinion(final T minion,
                                                               final MinionWorldPositions<T> worldPositions) {
        final Optional<MinionChunkPositions<T>> chunkPositionsOptional =
                this.getMinionChunksPosition(minion.getPosition(), worldPositions);

        if (minion instanceof Scheduleable) {
            this.schedulerMinionMap.remove(minion.getId());
        }

        if (chunkPositionsOptional.isEmpty()) {
            return Optional.empty();
        }

        final Optional<T> removed = chunkPositionsOptional.get().removeMinion(minion.getPosition());

        if (removed.isEmpty()) {
            return removed;
        }

        this.allMinions.remove(removed.get().getId());

        return removed;
    }

    public void startSchedulerMinionTask() {
        // todo - change interval based on config value
        this.schedulerMinionTask = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            for (final Scheduleable minion : this.schedulerMinionMap.values()) {
                minion.run();
            }
        }, 20, 20);
    }
}
