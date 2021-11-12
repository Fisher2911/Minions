package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public abstract class EntityMinion extends BaseMinion<Entity> {

    public EntityMinion(final JavaPlugin plugin,
                        final Instant lastActionTime,
                        final Position position,
                        final MinionData minionData) {
        super(plugin, lastActionTime, position, minionData);
    }
}
