package io.github.fisher2911.minion;

import io.github.fisher2911.MinionsPlugin;
import io.github.fisher2911.minion.types.BaseMinion;

public class MinionScheduler {

    private final MinionsPlugin plugin;
    private final MinionManager<BaseMinion> minionManager;

    public MinionScheduler(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
    }
    

}
