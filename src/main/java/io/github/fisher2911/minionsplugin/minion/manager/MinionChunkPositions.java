package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionChunkPositions<T extends BaseMinion<?>> {

    private final Map<Long, MinionPositions<T>> minionMap;

    public MinionChunkPositions(final Map<Long, MinionPositions<T>> minionMap) {
        this.minionMap = minionMap;
    }

    public MinionChunkPositions() {
        this(new HashMap<>());
    }

    public void set(final Position position, final T minion) {
        final Optional<MinionPositions<T>> optional = this.get(position);

        if (optional.isEmpty()) {
            final Map<Position, T> map = new HashMap<>();
            map.put(position, minion);
            this.minionMap.put(position.getChunkKey(), new MinionPositions<>(
                    map
            ));
            return;
        }

        optional.get().set(minion);
    }

    public Optional<MinionPositions<T>> get(final Position position) {
        final long chunkKey = position.getChunkKey();
        return this.get(chunkKey);
    }

    public Optional<MinionPositions<T>> get(final long chunkKey) {
        return Optional.ofNullable(this.minionMap.get(chunkKey));
    }

    public Optional<T> getMinionAt(final Position position) {
        final Optional<MinionPositions<T>> optional = this.get(position);

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return optional.get().get(position);
    }

    public Optional<MinionPositions<T>> remove(final Position position) {
        return Optional.ofNullable(this.minionMap.remove(position.getChunkKey()));
    }

    public Optional<T> removeMinion(final Position position) {
        final Optional<MinionPositions<T>> optional = this.get(position);

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        return optional.get().remove(position);
    }

    public Map<Long, MinionPositions<T>> getMinionMap() {
        return this.minionMap;
    }

    @Override
    public String toString() {
        return "MinionChunkPositions{" +
                "minionMap=" + this.minionMap +
                '}';
    }
}
