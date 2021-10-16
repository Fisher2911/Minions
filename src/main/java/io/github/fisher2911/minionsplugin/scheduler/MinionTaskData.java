package io.github.fisher2911.minionsplugin.scheduler;

import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;

public class MinionTaskData<T extends BaseMinion<R>, R> {

    private final T minion;
    private final R data;

    public MinionTaskData(final T minion, final R data) {
        this.minion = minion;
        this.data = data;
    }

    public T getMinion() {
        return this.minion;
    }

    public R getData() {
        return this.data;
    }
}
