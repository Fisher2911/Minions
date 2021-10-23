package io.github.fisher2911.minionsplugin.scheduler;

import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.jetbrains.annotations.NotNull;

public class MinionTaskData<T extends BaseMinion<R>, R> {

    private final T minion;
    private final R data;

    public MinionTaskData(final @NotNull T minion, final @NotNull R data) {
        this.minion = minion;
        this.data = data;
    }

    public @NotNull T getMinion() {
        return this.minion;
    }

    public @NotNull R getData() {
        return this.data;
    }
}
