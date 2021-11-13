package io.github.fisher2911.minionsplugin.minion.data;

import io.github.fisher2911.minionsplugin.minion.MinionType;

public enum MinionClass {

    MINER(MinionType.BLOCK),

    FARMER(MinionType.BLOCK),

    SLAYER(MinionType.ENTITY),

    WOODCUTTER(MinionType.BLOCK);

    private final MinionType minionType;

    MinionClass(final MinionType minionType) {
        this.minionType = minionType;
    }

    public MinionType getMinionType() {
        return this.minionType;
    }
}
