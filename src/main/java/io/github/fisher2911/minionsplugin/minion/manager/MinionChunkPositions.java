package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionChunkPositions<T extends BaseMinion<?>> {

    /**
     * Map of chunk key to {@link io.github.fisher2911.minionsplugin.minion.manager.MinionStorage}
     */
    private final Map<Long, MinionStorage<T>> minionMap;

    public MinionChunkPositions(final Map<Long, MinionStorage<T>> minionMap) {
        this.minionMap = minionMap;
    }

    public MinionChunkPositions() {
        this(new HashMap<>());
    }

    /**
     *
     * @param position {@link io.github.fisher2911.fishcore.world.Position} the minion is being set to in
     * the corresponding {@link io.github.fisher2911.minionsplugin.minion.manager.MinionStorage} at
     * the chunk key.
     * @param minion The minion that is being set
     */
    public void set(final Position position, final T minion) {
        final Optional<MinionStorage<T>> optional = this.get(position);

        if (optional.isEmpty()) {
            final Map<Long, T> map = new HashMap<>();
            map.put(minion.getId(), minion);
            this.minionMap.put(position.getChunkKey(), new MinionStorage<>(
                    map
            ));
            return;
        }

        optional.get().set(minion);
    }

    /**
     *
     * @param position the position whose {@link io.github.fisher2911.minionsplugin.minion.manager.MinionStorage} is being retrieved
     * @return the {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} the position's chunk key links to
     */
    public Optional<MinionStorage<T>> get(final Position position) {
        final long chunkKey = position.getChunkKey();
        return this.get(chunkKey);
    }

    /**
     *
     * @param chunkKey the chunk key whose {@link io.github.fisher2911.minionsplugin.minion.manager.MinionStorage} is being retrieved
     * @return the {@link io.github.fisher2911.minionsplugin.minion.manager.MinionChunkPositions} the chunk key links to
     */
    public Optional<MinionStorage<T>> get(final long chunkKey) {
        return Optional.ofNullable(this.minionMap.get(chunkKey));
    }

    /**
     *
     * @param position position of the minion being retrieved
     * @param id The id of the minion being retrieved
     * @return returns the minion with the id specified
     */
    public Optional<T> getMinionWithId(final Position position, final long id) {
        final Optional<MinionStorage<T>> optional = this.get(position.getChunkKey());

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return optional.get().get(id);
    }

    /**
     *
     * @param position The {@link io.github.fisher2911.fishcore.world.Position}'s chunk key being removed
     *                 Used mostly for when chunks unload
     * @return The {@link io.github.fisher2911.minionsplugin.minion.manager.MinionStorage} being removed
     */
    public Optional<MinionStorage<T>> remove(final Position position) {
        return Optional.ofNullable(this.minionMap.remove(position.getChunkKey()));
    }

    /**
     *
     * @param position The {@link io.github.fisher2911.fishcore.world.Position} that the minion is at
     * @param id the id of the minion
     * @return the removed minion
     */
    public Optional<T> removeMinion(final Position position, final long id) {
        final Optional<MinionStorage<T>> optional = this.get(position);

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return optional.get().remove(id);
    }

    /**
     *
     * @return the map of chunk key to {@link io.github.fisher2911.minionsplugin.minion.manager.MinionStorage}
     */

    public Map<Long, MinionStorage<T>> getMinionMap() {
        return this.minionMap;
    }

    @Override
    public String toString() {
        return "MinionChunkPositions{" +
                "minionMap=" + this.minionMap +
                '}';
    }
}
