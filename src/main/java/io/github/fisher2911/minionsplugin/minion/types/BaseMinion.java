package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.minionsplugin.exception.MinionException;
import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.keys.Keys;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseMinion<T> implements IdHolder<Long>, InventoryHolder {

    protected final JavaPlugin plugin;
    private LocalDateTime lastActionTime;
    protected ArmorStand minion;
    protected final long id;
    protected final UUID owner;
    protected final Region region;
    private final MinionData minionData;

    public BaseMinion(final JavaPlugin plugin,
                      final LocalDateTime lastActionTime,
                      final long id,
                      final UUID owner,
                      final Region region,
                      final MinionData minionData) {
        this.plugin = plugin;
        this.lastActionTime = lastActionTime;
        this.id = id;
        this.owner = owner;
        this.region = region;
        this.minionData = minionData;
    }

    public abstract boolean performAction(final T t);

    public boolean isPlaced() {
        return this.minion != null && this.minion.isValid();
    }

    public void place() {
        final Location location = this.getPosition().toBukkitLocation();
        if (location == null) {
           throw new MinionException("Attempted to place minion with location that does not have a valid world");
        }

        final World world = location.getWorld();

        if (world == null) {
            return;
        }

        this.minion = world.spawn(location, ArmorStand.class, entity -> {
           entity.getPersistentDataContainer().set(Keys.MINION_KEY, PersistentDataType.LONG, this.id);
           entity.setCustomName(this.minionData.getName());
           entity.setCustomNameVisible(true);
           entity.setSmall(true);
           entity.setArms(true);
           entity.setGravity(false);
           entity.setInvulnerable(true);
           this.minionData.getInventory().setArmor(entity);

        });
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public ArmorStand getMinion() {
        return this.minion;
    }

    public void despawn() {
        if (!this.isPlaced()) {
            return;
        }

        this.minion.remove();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.minionData.getInventory().getInventory();
    }

    public Position getPosition() {
        return this.region.getOrigin();
    }

    public void setLastActionTime(final LocalDateTime lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public LocalDateTime getLastActionTime() {
        return this.lastActionTime;
    }

    public boolean canPerformAction() {
        // todo remove hard coded speed
        final int speedInSeconds = 1;

        return Duration.between(this.lastActionTime, LocalDateTime.now()).getSeconds() >= speedInSeconds;
    }
}
