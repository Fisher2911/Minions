package io.github.fisher2911.minionsplugin.command;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.lang.PlaceholderMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public abstract class BaseCommand {

    final MinionsPlugin plugin;
    final String name;
    final String permission;
    final Set<Executor> executors;

    public BaseCommand(
            final MinionsPlugin plugin,
            final String name,
            final String permission,
            final Set<Executor> executors) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission;
        this.executors = executors;
    }

    /**
     *
     * @return list of messages to be sent to the player
     */
    abstract List<PlaceholderMessage> execute(final CommandSender sender, final String[] args);

    abstract List<String> getCompletions(final String[] args);

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public Set<Executor> getExecutors() {
        return this.executors;
    }

    public boolean isAllowedExecutor(final CommandSender sender) {
        return this.executors.contains(Executor.fromBukkitExecutor(sender));
    }

    public enum Executor {

        PLAYER,

        CONSOLE,

        OTHER;

        public static Executor fromBukkitExecutor(final CommandSender object) {
            if (object instanceof Player) {
                return PLAYER;
            }

            if (object instanceof ConsoleCommandSender) {
                return CONSOLE;
            }

            return OTHER;
        }

    }
}
