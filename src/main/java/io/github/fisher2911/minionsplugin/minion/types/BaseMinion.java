package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.keys.Keys;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.food.FeedResponse;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public abstract class BaseMinion<T> implements IdHolder<Long> {

    protected final JavaPlugin plugin;
    private Instant lastActionTime;
    protected ArmorStand minion;
    protected final long id;
    protected final UUID owner;
    protected MinionType minionType;
    protected final Position position;
    private final MinionData minionData;

    public BaseMinion(
            final JavaPlugin plugin,
            final Instant lastActionTime,
            final long id,
            final UUID owner,
            final MinionType minionType,
            final Position position,
            final MinionData minionData) {
        this.plugin = plugin;
        this.lastActionTime = lastActionTime;
        this.id = id;
        this.minionType = minionType;
        this.owner = owner;
        this.position = position;
        this.minionData = minionData;
    }

    // Performs action even if still on cooldown
    protected abstract ActionResult performAction(final T t);

    // Takes into account things like food, time passed, in region
    public boolean attemptAction(final T t, final Position position) {
        final FoodData foodData = this.minionData.getFoodData();

        if (!this.isPlaced()) {
            return false;
        }

        if (!foodData.hasFood()) {
            return false;
        }

        if (!this.enoughTimePassed()) {
            return false;
        }

        if (!this.isInRegion(position)) {
            return false;
        }

        foodData.decreaseFood(this.getUpgrades().getFoodPerAction());
        this.performAction(t);
        this.setLastActionTime(Instant.now());
        return true;
    }

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

    public void setLastActionTime(final Instant lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public Instant getLastActionTime() {
        return this.lastActionTime;
    }

    public boolean isInRegion(final Position position) {
        return this.getRegion().contains(position);
    }

    public boolean enoughTimePassed() {
        final float speed = this.getUpgrades().getSpeed();
        return Duration.between(this.lastActionTime, Instant.now()).getSeconds() >= speed;
    }

    public Region getRegion() {
        return this.getUpgrades().getRange().toRegion(this.position);
    }

    // todo - change message
    public FeedResponse feed(final MinionUser feeder, final ItemStack fedItem) {
        final FeedResponse feedResponse = this.minionData.feed(fedItem);
        feeder.ifOnline(player -> player.sendMessage(feedResponse.toString()));
        return feedResponse;
    }

    public Upgrades getUpgrades() {
        return this.minionData.getUpgrades();
    }

    public UUID getOwner() {
        return this.owner;
    }

    public MinionData getMinionData() {
        return this.minionData;
    }
}
