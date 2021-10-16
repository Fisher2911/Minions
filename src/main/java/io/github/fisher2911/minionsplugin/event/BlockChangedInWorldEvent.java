package io.github.fisher2911.minionsplugin.event;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockChangedInWorldEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Block block;
    private final Type type;

    public BlockChangedInWorldEvent(final Block block, final Type type) {
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
