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

    public EntityMinion(final @NotNull JavaPlugin plugin,
                        final @NotNull LocalDateTime lastActionTime,
                        final long id,
                        final @NotNull UUID owner,
                        final @NotNull Region region,
                        final @NotNull MinionData minionData,
                        final @NotNull Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, region, minionData, upgrades);
    }
}
