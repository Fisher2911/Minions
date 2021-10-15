package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.MinionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionManager minionManager;

    public BlockBreakListener(final MinionsPlugin plugin, final MinionManager minionManager) {
        this.plugin = plugin;
        this.minionManager = minionManager;
    }

    @EventHandler
    public void onBlockBreak() {

    }
}
