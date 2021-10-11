package io.github.fisher2911.minion.types;

import io.github.fisher2911.exception.MinionException;
import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.keys.Keys;
import io.github.fisher2911.minion.MinionData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public abstract class BaseMinion implements IdHolder<Long> {

    protected final long id;
    protected final UUID owner;
    protected final Position position;
    private final MinionData minionData;

    public BaseMinion(final long id, final UUID owner, final Position position,
                      final MinionData minionData) {
        this.id = id;
        this.owner = owner;
        this.position = position;
        this.minionData = minionData;
    }

    public abstract void performAction();

    public void place() {
        final Location location = this.position.toBukkitLocation();
        if (location == null) {
           throw new MinionException("Attempted to place minion with location that does not have a valid world");
        }

        final World world = location.getWorld();

        if (world == null) {
            return;
        }

        world.spawn(location, ArmorStand.class, entity -> {
           entity.getPersistentDataContainer().set(Keys.MINION_KEY, PersistentDataType.LONG, this.id);
           this.minionData.getInventory().setArmor(entity);
           entity.setCustomName(this.minionData.getName());
        });
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
