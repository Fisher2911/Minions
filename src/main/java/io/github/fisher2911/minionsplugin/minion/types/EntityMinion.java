package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.UUID;

public abstract class EntityMinion extends BaseMinion<Entity> {

    public EntityMinion(final JavaPlugin plugin,
                        final Instant lastActionTime,
                        final long id,
                        final UUID owner,
                        final MinionType minionType,
                        final Position position,
                        final MinionData minionData) {
        super(plugin, lastActionTime, id, owner, minionType, position, minionData);
    }
}
