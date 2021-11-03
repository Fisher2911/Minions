package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.keys.Keys;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseMinion<T> implements IdHolder<Long> {

    protected final JavaPlugin plugin;
    private LocalDateTime lastActionTime;
    protected ArmorStand minion;
    protected final long id;
    protected final UUID owner;
    protected MinionType minionType;
    protected final Position position;
    private final MinionData minionData;
    private final Upgrades upgrades;

    public BaseMinion(
            final JavaPlugin plugin,
            final LocalDateTime lastActionTime,
            final long id,
            final UUID owner,
            final MinionType minionType,
            final Position position,
            final MinionData minionData,
            final Upgrades upgrades) {
        this.plugin = plugin;
        this.lastActionTime = lastActionTime;
        this.id = id;
        this.minionType = minionType;
        this.owner = owner;
        this.position = position;
        this.minionData = minionData;
        this.upgrades = upgrades;
    }

    public abstract boolean performAction(final T t);

    public boolean isPlaced() {
        return this.minion != null && this.minion.isValid();
    }

    public void place() {
        final Location location = this.getPosition().toBukkitLocation();

        final World world = location.getWorld();

        if (world == null) {
            return;
        }

        this.minion = world.spawn(location, ArmorStand.class, entity -> {
            final PersistentDataContainer container = entity.getPersistentDataContainer();
            container.set(Keys.MINION_KEY, PersistentDataType.LONG, this.id);
            container.set(Keys.MINION_TYPE_KEY, PersistentDataType.STRING, this.minionType.toString());

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

    public MinionType getMinionType() {
        return this.minionType;
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

    public MinionInventory getInventory() {
        return this.minionData.getInventory();
    }

    public Position getPosition() {
        return this.position;
    }

    public void setLastActionTime(final LocalDateTime lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public LocalDateTime getLastActionTime() {
        return this.lastActionTime;
    }

    public boolean canPerformAction() {
        final float speed = this.upgrades.getSpeed();

        this.plugin.getLogger().info("Minion Speed is: " + speed);

        return Duration.between(this.lastActionTime, LocalDateTime.now()).getSeconds() >= speed;
    }

    public Upgrades getUpgrades() {
        return this.upgrades;
    }

    public Region getRegion() {
        final Region region = this.getUpgrades().getRange().toRegion(this.position);
        Bukkit.getLogger().info("Region: " + region);
        Bukkit.getLogger().info("Range: " + this.getUpgrades().getRange());
        return region;
    }
}
