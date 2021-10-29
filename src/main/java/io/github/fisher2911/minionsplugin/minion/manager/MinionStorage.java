package io.github.fisher2911.minionsplugin.minion.manager;

import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinionStorage<T extends BaseMinion<?>> {

    /**
     * Map of Minion Id to minion
     */
    private final Map<Long, T> minionMap;

    public MinionStorage(final Map<Long, T> minionMap) {
        this.minionMap = minionMap;
    }

    public MinionStorage() {
        this(new HashMap<>());
    }

    /**
     *
     * @param id the id of the minion being retrieved
     * @return the minion retrieved
     */
    public Optional<T> get(final long id) {
        return Optional.ofNullable(this.minionMap.get(id));
    }

    /**
     *
     * @param minion adds a minion
     */
    public void set(final T minion) {
        this.minionMap.put(minion.getId(), minion);
    }

    /**
     *
     * @param id the id of the minion being removed
     * @return the removed minion
     */
    public Optional<T> remove(final long id) {
        return Optional.ofNullable(this.minionMap.remove(id));
    }

    /**
     *
     * @return all of the minion id's and minions
     */
    public Map<Long, T> getMinionMap() {
        return this.minionMap;
    }

    @Override
    public String toString() {
        return "MinionStorage{" +
                "minionMap=" + this.minionMap +
                '}';
    }
}
