package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BlockMinion extends BaseMinion<BlockChangedInWorldEvent> {

    public BlockMinion(final JavaPlugin plugin,
                       final LocalDateTime lastActionTime,
                       final long id,
                       final UUID owner,
                       final MinionType minionType,
                       final Position position,
                       final MinionData minionData,
                       final Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, minionType, position, minionData, upgrades);
    }
}
