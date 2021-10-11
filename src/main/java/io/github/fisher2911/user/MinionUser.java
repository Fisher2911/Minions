package io.github.fisher2911.user;


import io.github.fisher2911.fishcore.user.BaseUser;

import java.util.Set;
import java.util.UUID;

public class MinionUser extends BaseUser {

    private final Set<Long> ownedMinions;

    public MinionUser(final UUID uuid, final Set<Long> ownedMinions) {
        super(uuid);
        this.ownedMinions = ownedMinions;
    }
}
