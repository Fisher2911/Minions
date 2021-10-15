package io.github.fisher2911.minionsplugin.minion;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.keys.Keys;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MinionManager {

    private final Map<Long, Map<Long, BaseMinion>> minionMap = new HashMap<>();

    public Optional<BaseMinion> getMinion(final ArmorStand armorStand) {
        final long id = this.getId(armorStand);

        final Position position = Position.fromBukkitLocation(armorStand.getLocation());

        final long chunkKey = position.getChunkKey();

        final Optional<Map<Long, BaseMinion>> minionsInChunk = this.getMinionsInChunk(chunkKey);

        if (minionsInChunk.isEmpty() || minionsInChunk.get().isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(minionsInChunk.get().get(id));
    }

    public Optional<Map<Long, BaseMinion>> getMinionsInChunk(final Long chunkKey) {
        return Optional.ofNullable(this.minionMap.get(chunkKey));
    }

    public void addMinion(final BaseMinion baseMinion) {
        final Position position = baseMinion.getPosition();
        final long chunkKey = position.getChunkKey();
        final Optional<Map<Long, BaseMinion>> minionsInChunkOptional = this.getMinionsInChunk(chunkKey);

        minionsInChunkOptional.ifPresentOrElse(map -> map.put(baseMinion.getId(), baseMinion),
                () -> {
                    final Map<Long, BaseMinion> newMap = new HashMap<>();
                    newMap.put(baseMinion.getId(), baseMinion);
                    this.minionMap.put(chunkKey, newMap);
                }
        );
    }

    public void removeMinion(final ArmorStand armorStand) {
        final long id = this.getId(armorStand);

        if (id == -1) {
            return;
        }

        final Position position = Position.fromBukkitLocation(armorStand.getLocation());

        final long chunkKey = position.getChunkKey();

        final Optional<Map<Long, BaseMinion>> minionsInChunkOptional = this.getMinionsInChunk(chunkKey);

        minionsInChunkOptional.ifPresent(map -> map.remove(id));
    }

    public void removeMinionsInChunk(final long chunkKey) {
        final Optional<Map<Long, BaseMinion>> baseMinionMap = this.getMinionsInChunk(chunkKey);
        baseMinionMap.ifPresent(map -> {
            for (final BaseMinion minion : map.values()) {
                minion.despawn();
            }
            map.clear();
        });
        this.minionMap.remove(chunkKey);
    }

    public Set<BaseMinion> getAll() {
        final Set<BaseMinion> baseMinions = new HashSet<>();
        for (final Map<Long, BaseMinion> map : this.minionMap.values()) {
            baseMinions.addAll(map.values());
        }
        return baseMinions;
    }

    public long getId(final ArmorStand armorStand) {
        final PersistentDataContainer container = armorStand.getPersistentDataContainer();

        final Long id = container.get(Keys.MINION_KEY, PersistentDataType.LONG);

        return id == null ? -1 : id;
    }

}
