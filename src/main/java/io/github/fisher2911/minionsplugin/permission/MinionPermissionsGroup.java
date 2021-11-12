package io.github.fisher2911.minionsplugin.permission;

import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MinionPermissionsGroup implements Comparable<MinionPermissionsGroup> {

    private int ranking;
    private String name;
    private Mode mode;
    private Set<Long> minions;
    private List<UUID> members;
    private final MinionPermissions minionPermissions;

    public MinionPermissionsGroup(
            final int ranking,
            final String name,
            final Mode mode,
            final Set<Long> minions,
            final List<UUID> members,
            final MinionPermissions minionPermissions) {
        this.ranking = ranking;
        this.name = name;
        this.mode = mode;
        this.minions = minions;
        this.members = members;
        this.minionPermissions = minionPermissions;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    public List<UUID> getMembers() {
        return this.members;
    }

    public void setMembers(final List<UUID> members) {
        this.members = members;
    }

    public int getRanking() {
        return this.ranking;
    }

    public void setRanking(final int ranking) {
        this.ranking = ranking;
    }

    public MinionPermissions getMinionPermissions() {
        return this.minionPermissions;
    }

    public Set<Long> getMinions() {
        return this.minions;
    }

    public boolean appliesToMinion(final BaseMinion<?> minion) {
        return this.minions.contains(minion.getId());
    }

    public boolean hasPermission(
            final UUID uuid,
            final String permission) {

        final boolean hasUuid = this.members.contains(uuid);

        return switch (this.mode) {
            case SPECIFIED -> {
                if (!hasUuid) {
                    yield false;
                }

                yield this.minionPermissions.hasPermission(permission);
            }

            case NOT_SPECIFIED -> this.minionPermissions.hasPermission(permission);
        };
    }

    @Override
    public int compareTo(@NotNull final MinionPermissionsGroup other) {
        return Integer.compare(this.getRanking(), other.getRanking());
    }

    public enum Mode {

        /**
         * Applies to players explicitly defined
         */
        SPECIFIED,

        /**
         * Applies to both players explicitly and not explicitly defined
         */
        NOT_SPECIFIED

    }
}
