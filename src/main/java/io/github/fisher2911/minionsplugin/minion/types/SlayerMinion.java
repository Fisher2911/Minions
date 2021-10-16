package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.util.RandomUtil;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SlayerMinion extends EntityMinion {

    public SlayerMinion(final JavaPlugin plugin,
                        final LocalDateTime lastActionTime,
                        final long id,
                        final UUID owner,
                        final Region region,
                        final MinionData minionData) {
        super(plugin, lastActionTime, id, owner, region, minionData);
    }

    final Set<EntityType> allowedEntities =
            EnumSet.of(EntityType.SHEEP, EntityType.COW, EntityType.PIG, EntityType.CHICKEN);

    @Override
    public boolean performAction(final Entity entity) {

        if (!this.canPerformAction()) {
            return false;
        }

        final Position position = this.getPosition();

        final Optional<World> worldOptional = position.getWorld();
        if (worldOptional.isEmpty()) {
            return true;
        }

        final World world = worldOptional.get();

        final List<LivingEntity> livingEntities =
                world.getNearbyEntities(position.toBukkitLocation(), 5, 5, 5).
                        stream().
                        filter(LivingEntity.class::isInstance).
                        filter(nearby -> this.allowedEntities.contains(nearby.getType())).
                        map(LivingEntity.class::cast).
                        collect(Collectors.toList());

        if (livingEntities.isEmpty()) {
            return true;
        }

        final int rand = RandomUtil.rand(livingEntities.size());
        final LivingEntity livingEntity = livingEntities.get(rand);

        if (!isPlaced()) {
            return true;
        }

        final ArmorStand minion = this.getMinion();

        livingEntity.damage(livingEntity.getHealth(), minion);
        return true;
    }

}
