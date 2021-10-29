package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class EntityMinion extends BaseMinion<Entity> {

    public EntityMinion(final JavaPlugin plugin,
                        final LocalDateTime lastActionTime,
                        final long id,
                        final UUID owner,
                        final Region region,
                        final MinionData minionData,
                        final Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, region, minionData, upgrades);
    }
}
