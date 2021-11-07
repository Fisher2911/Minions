package io.github.fisher2911.minionsplugin.model;

import io.github.fisher2911.minionsplugin.user.MinionUser;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MinionUserCache {
    private final Map<UUID, MinionUser> map;

    public MinionUserCache(Map<UUID, MinionUser> map) {
        this.map = map;
    }

    public MinionUser getOrCreate(UUID uniqueId) {
        return this.map.computeIfAbsent(uniqueId, MinionUser::new);
    }

    public Optional<MinionUser> get(UUID uniqueId) {
        return Optional.ofNullable(this.map.get(uniqueId));
    }

    public void invalidate(UUID uniqueId) {
        this.map.remove(uniqueId);
    }
}
