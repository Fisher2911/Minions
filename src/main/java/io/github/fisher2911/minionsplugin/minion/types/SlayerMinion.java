package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

public class SlayerMinion extends EntityMinion {

    public SlayerMinion(final JavaPlugin plugin,
                        final Instant lastActionTime,
                        final Position position,
                        final MinionData minionData) {
        super(plugin, lastActionTime, position, minionData);
    }

    final Set<EntityType> allowedEntities =
            EnumSet.of(EntityType.SHEEP, EntityType.COW, EntityType.PIG, EntityType.CHICKEN);

    @Override
    public ActionResult performAction(final Entity entity) {
        final ArmorStand minion = this.getMinion();

        if (entity instanceof final LivingEntity livingEntity) {

            livingEntity.damage(livingEntity.getHealth(), minion);
            return ActionResult.SUCCESS;
        }
        return ActionResult.NOT_POSSIBLE;
    }
}
