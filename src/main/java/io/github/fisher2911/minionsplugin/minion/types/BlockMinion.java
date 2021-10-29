package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BlockMinion extends BaseMinion<BlockChangedInWorldEvent> {

    public BlockMinion(final JavaPlugin plugin,
                       final LocalDateTime lastActionTime,
                       final long id,
                       final UUID owner,
                       final Region region,
                       final MinionData minionData,
                       final Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, region, minionData, upgrades);
    }
}
