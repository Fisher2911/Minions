package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MinionWorldPositions<T extends BaseMinion<?>> {

    /**
     * Map of world UUID to {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions}
     */
    private final Map<UUID, MinionChunkPositions<T>> positionsMap;

    public MinionWorldPositions(final Map<UUID, MinionChunkPositions<T>> positionsMap) {
        this.positionsMap = positionsMap;
    }

    public MinionWorldPositions() {
        this(new HashMap<>());
    }

    /**
     *
     * @param minion Sets the minion in a {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions}
     */
    public void set(final T minion) {
        final Position position = minion.getPosition();

        final Optional<World> optional = position.getWorld();

        if (optional.isEmpty()) {
            return;
        }

        final World world = optional.get();

        final Optional<MinionChunkPositions<T>> optionalChunkPositions = this.
                get(world);

        if (optionalChunkPositions.isEmpty()) {
            final Map<Long, T> minionMap = new HashMap<>();
            minionMap.put(minion.getId(), minion);

            final MinionStorage<T> minionStorage = new MinionStorage<>(minionMap);

            final Map<Long, MinionStorage<T>> minionPositionsMap = new HashMap<>();
            minionPositionsMap.put(position.getChunkKey(), minionStorage);

            final MinionChunkPositions<T> minionChunkPositions =
                    new MinionChunkPositions<>(minionPositionsMap);
            this.positionsMap.put(world.getUID(), minionChunkPositions);
            return;
        }

        optionalChunkPositions.get().set(position, minion);
    }

    /**
     *
     * @param world the world the {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} is in
     * @return The {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} in the world
     */
    public Optional<MinionChunkPositions<T>> get(final World world) {
        return this.get(world.getUID());
    }

    /**
     *
     * @param worldUUID the world uuid the {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} is in
     * @return The {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} in the world
     */
    public Optional<MinionChunkPositions<T>> get(final UUID worldUUID) {
        return Optional.ofNullable(this.positionsMap.get(worldUUID));
    }

    /**
     *
     * @param world world {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} are being removed from
     * @return the {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} removed
     */
    public Optional<MinionChunkPositions<T>> remove(final World world) {
        return this.remove(world.getUID());
    }


    /**
     *
     * @param worldUUID {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} are being removed from
     * @return the {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} removed
     */
    public Optional<MinionChunkPositions<T>> remove(final UUID worldUUID) {
        return Optional.ofNullable(this.positionsMap.remove(worldUUID));
    }

    @Override
    public String toString() {
        return "MinionWorldPositions{" +
                "positionsMap=" + this.positionsMap +
                '}';
    }
}
