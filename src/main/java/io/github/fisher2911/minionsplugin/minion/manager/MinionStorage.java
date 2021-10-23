package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionStorage<T extends BaseMinion<?>> {

    private final Map<Long, T> minionMap;


    public MinionStorage(final @NotNull Map<Long, T> minionMap) {
        this.minionMap = minionMap;
    }

    public MinionStorage() {
        this(new HashMap<>());
    }


    public Optional<T> get(final long id) {
        return Optional.ofNullable(this.minionMap.get(id));
    }

    public void set(final @NotNull T minion) {
        this.minionMap.put(minion.getId(), minion);
    }

    public Optional<T> remove(final long id) {
        return Optional.ofNullable(this.minionMap.remove(id));
    }

    public @NotNull Map<Long, T> getMinionMap() {
        return this.minionMap;
    }

    @Override
    public String toString() {
        return "MinionStorage{" +
                "minionMap=" + this.minionMap +
                '}';
    }
}
