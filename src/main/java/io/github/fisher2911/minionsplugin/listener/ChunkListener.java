package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.util.PositionUtil;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionManager minionManager;

    public ChunkListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
    }

    @EventHandler
    public void onChunkUnload(final ChunkUnloadEvent event) {
        final Chunk chunk = event.getChunk();
        final long key = PositionUtil.getChunkKey(chunk);
        this.minionManager.removeMinionsInChunk(key);
    }
}
