package io.github.fisher2911.minionsplugin.event;

import io.github.fisher2911.fishcore.world.Position;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

// Used for block minions actions
public class BlockChangedInWorldEvent extends PositionEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    // Block changed
    private final Block block;

    // Whether it is added or removed from the world
    private final Type type;

    public BlockChangedInWorldEvent(
            final Position position,
            final Block block,
            final Type type) {
        super(position);
        this.block = block;
        this.type = type;
    }

    public Block getBlock() {
        return this.block;
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {

        ADDED,

        REMOVED

    }
}
