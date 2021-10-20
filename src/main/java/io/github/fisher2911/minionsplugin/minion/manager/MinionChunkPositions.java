package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionChunkPositions<T extends BaseMinion<?>> {

    private final Map<Long, MinionStorage<T>> minionMap;

    public MinionChunkPositions(final Map<Long, MinionStorage<T>> minionMap) {
        this.minionMap = minionMap;
    }

    public MinionChunkPositions() {
        this(new HashMap<>());
    }

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

    public Optional<MinionStorage<T>> get(final Position position) {
        final long chunkKey = position.getChunkKey();
        return this.get(chunkKey);
    }

    public Optional<MinionStorage<T>> get(final long chunkKey) {
        return Optional.ofNullable(this.minionMap.get(chunkKey));
    }

    public Optional<T> getMinionWithId(final Long id) {
        final Optional<MinionStorage<T>> optional = this.get(id);

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return optional.get().get(id);
    }

    public Optional<MinionStorage<T>> remove(final Position position) {
        return Optional.ofNullable(this.minionMap.remove(position.getChunkKey()));
    }

    public Optional<T> removeMinion(final Position position, final long id) {
        final Optional<MinionStorage<T>> optional = this.get(position);

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return optional.get().remove(id);
    }

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
