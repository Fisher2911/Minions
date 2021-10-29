package io.github.fisher2911.minionsplugin.scheduler;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

public class MinionScheduler<T extends BaseMinion<R>, R> {

    private final Queue<MinionTaskData<T, R>> minionQueue = new LinkedList<>();

    private final MinionsPlugin plugin;

    private boolean active = false;
    private BukkitTask task;

    public MinionScheduler(final MinionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void addMinionTaskData(final MinionTaskData<T, R> taskData) {
        this.minionQueue.add(taskData);


        if (this.minionQueue.size() == 1) {
            this.start();
        }
    }

    public void start() {
        if (this.active) {
            return;
        }
        this.setTask();
        this.active = true;
    }

    private void setTask() {
        // todo remove hard coded loop time, get value from config file
        this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            final Queue<MinionTaskData<T, R>> checked = new LinkedList<>();

            MinionTaskData<T, R> taskData;
            while ((taskData = this.minionQueue.poll()) != null) {

                final T minion = taskData.getMinion();

                if (minion.performAction(taskData.getData())) {
                    continue;
                }

                checked.add(taskData);
            }

            if (checked.isEmpty()) {
                this.cancelTask();
                return;
            }

            this.minionQueue.addAll(checked);
        }, 20, 20);
    }

    private void cancelTask() {
        this.task.cancel();
        this.active = false;
    }

}
