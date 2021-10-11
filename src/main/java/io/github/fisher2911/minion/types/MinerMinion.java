package io.github.fisher2911.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minion.MinionData;

import java.util.UUID;

public class MinerMinion extends BaseMinion {

    public MinerMinion(final long id, final UUID owner, final Position position, final MinionData minionData) {
        super(id, owner, position, minionData);
    }

    @Override
    public void performAction() {

    }
    
}
