package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.util.RandomUtil;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SlayerMinion extends BaseMinion {

    public SlayerMinion(final JavaPlugin plugin, final long id, final UUID owner, final Position position, final MinionData minionData) {
        super(plugin, id, owner, position, minionData);
    }

    final Set<EntityType> allowedEntities =
            EnumSet.of(EntityType.SHEEP, EntityType.COW, EntityType.PIG, EntityType.CHICKEN);

    @Override
    public void performAction() {
        final Optional<World> worldOptional = this.position.getWorld();
        if (worldOptional.isEmpty()) {
            return;
        }

        final World world = worldOptional.get();

        final List<LivingEntity> livingEntities =
                world.getNearbyEntities(this.position.toBukkitLocation(), 5, 5, 5).
                        stream().
                        filter(LivingEntity.class::isInstance).
                        filter(entity -> this.allowedEntities.contains(entity.getType())).
                        map(LivingEntity.class::cast).
                        collect(Collectors.toList());

        if (livingEntities.isEmpty()) {
            return;
        }

        final int rand = RandomUtil.rand(livingEntities.size());
        final LivingEntity entity = livingEntities.get(rand);

        if (!isPlaced()) {
            return;
        }

        final ArmorStand minion = this.getMinion();

        entity.damage(entity.getHealth(), minion);
    }

}
