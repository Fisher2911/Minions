package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionPositions<T extends BaseMinion<?>> {

    private final Map<Position, T> minionMap;


    public MinionPositions(final Map<Position, T> minionMap) {
        this.minionMap = minionMap;
    }

    public MinionPositions() {
        this(new HashMap<>());
    }


    public Optional<T> get(final Position position) {
        return Optional.ofNullable(this.minionMap.get(position));
    }

    public void set(final T minion) {
        this.minionMap.put(minion.getPosition(), minion);
    }

    public Optional<T> remove(final Position position) {
        return Optional.ofNullable(this.minionMap.remove(position));
    }

    public Map<Position, T> getMinionMap() {
        return this.minionMap;
    }

    @Override
    public String toString() {
        return "MinionPositions{" +
                "minionMap=" + this.minionMap +
                '}';
    }
}
