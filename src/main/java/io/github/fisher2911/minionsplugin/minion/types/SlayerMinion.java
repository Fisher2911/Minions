package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class SlayerMinion extends EntityMinion {

    public SlayerMinion(final JavaPlugin plugin,
                        final Instant lastActionTime,
                        final long id,
                        final UUID owner,
                        final MinionType minionType,
                        final Position position,
                        final MinionData minionData,
                        final Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, minionType, position, minionData, upgrades);
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
