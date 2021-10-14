package io.github.fisher2911.minionsplugin.user;


import io.github.fisher2911.fishcore.user.BaseUser;

import java.util.Set;
import java.util.UUID;

public class MinionUser extends BaseUser {

    private final Set<Long> ownedMinions;

    public MinionUser(final UUID uuid, final Set<Long> ownedMinions) {
        super(uuid);
        this.ownedMinions = ownedMinions;
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