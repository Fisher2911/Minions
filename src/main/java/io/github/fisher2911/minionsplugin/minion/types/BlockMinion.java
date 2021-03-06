package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BlockMinion extends BaseMinion<BlockChangedInWorldEvent> {

    public BlockMinion(final JavaPlugin plugin,
                       final Position position,
                       final MinionData minionData) {
        super(plugin, position, minionData);
    }
}
