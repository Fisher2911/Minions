package io.github.fisher2911.minionsplugin.user;


import io.github.fisher2911.fishcore.user.BaseUser;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MinionUser extends BaseUser {

    /**
     * A set of all minions the user owns
     */
    private final Set<Long> ownedMinions;
    private final Map<String, MinionPermissionsGroup> minionPermissionsGroups;

    public MinionUser(final UUID uuid,
                      final String name,
                      final Set<Long> ownedMinions,
                      final Map<String, MinionPermissionsGroup> minionPermissionsGroups) {
        super(uuid, name);
        this.ownedMinions = ownedMinions;
        this.minionPermissionsGroups = minionPermissionsGroups;
    }

    public MinionUser(
            final UUID uuid,
            final double money,
            final String name,
            final Set<Long> ownedMinions,
            final Map<String, MinionPermissionsGroup> minionPermissionsGroups) {
        super(uuid, money, name);
        this.ownedMinions = ownedMinions;
        this.minionPermissionsGroups = minionPermissionsGroups;
    }

    @Unmodifiable
    public Set<Long> getOwnedMinions() {
        return Collections.unmodifiableSet(this.ownedMinions);
    }

    public Map<String, MinionPermissionsGroup> getMinionPermissionsGroups() {
        return this.minionPermissionsGroups;
    }

    public void addMinionPermissionsGroup(final MinionPermissionsGroup minionPermissionsGroup) {
        this.minionPermissionsGroups.put(
                minionPermissionsGroup.getId(),
                minionPermissionsGroup
        );
    }

    @Override
    public double getMoney() {
        return 0;
    }

    @Override
    public void subtractMoney(final double money) {

    }

    @Override
    public void addMoney(final double money) {

    }
}
