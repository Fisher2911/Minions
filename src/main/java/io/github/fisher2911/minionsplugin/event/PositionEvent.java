package io.github.fisher2911.minionsplugin.event;

import io.github.fisher2911.fishcore.world.Position;
import org.bukkit.event.Event;

public abstract class PositionEvent extends Event {

    private final Position position;

    public PositionEvent(final Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }
}
