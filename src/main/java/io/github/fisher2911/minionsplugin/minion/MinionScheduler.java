package io.github.fisher2911.minionsplugin.minion;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class MinionScheduler {

    private final MinionsPlugin plugin;
    private final MinionManager<BaseMinion> minionManager;
    private BukkitTask task;

    public MinionScheduler(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            for (final BaseMinion baseMinion : this.minionManager.getAll()) {
                baseMinion.performAction();
            }
        }, 20, 20);
    }
}
