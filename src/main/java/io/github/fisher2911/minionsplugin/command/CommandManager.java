package io.github.fisher2911.minionsplugin.command;

import io.github.fisher2911.fishcore.message.MessageHandler;
import io.github.fisher2911.minionsplugin.lang.PlaceholderMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabExecutor {

    private final String name;
    private final MessageHandler messageHandler;
    private final BaseCommand defaultCommand;
    private final Map<String, BaseCommand> commandMap = new HashMap<>();

    private final List<String> sortedCommandNames = new ArrayList<>();

    public CommandManager(
            final String name,
            final MessageHandler messageHandler,
            final BaseCommand defaultCommand) {
        this.name = name;
        this.messageHandler = messageHandler;
        this.defaultCommand = defaultCommand;
    }

    public void register(final BaseCommand command) {
        final String name = command.getName();
        this.commandMap.put(name, command);
        this.sortedCommandNames.add(name);
        Collections.sort(this.sortedCommandNames);
    }

    @Override
    public boolean onCommand(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String label,
            @NotNull final String[] args) {

        final BaseCommand baseCommand;

        if (args.length == 0) {
            baseCommand = this.defaultCommand;
        } else {
            baseCommand = this.commandMap.get(args[0]);
        }

        if (baseCommand == null) {
            return true;
        }

        if (!baseCommand.isAllowedExecutor(sender)) {
            if (sender instanceof Player) {
                this.messageHandler.sendMessage(
                        sender,
                        Messages.MUST_BE_CONSOLE
                );
                return true;
            }

            this.messageHandler.sendMessage(
                    sender,
                    Messages.MUST_BE_PLAYER
            );
            return true;
        }

        final String[] actualArgs = this.getActualArgs(args);

        final List<PlaceholderMessage> messages = baseCommand.execute(
                sender,
                actualArgs);

        for (final PlaceholderMessage message : messages) {
            this.messageHandler.sendMessage(
                    sender,
                    message.message(),
                    message.placeholders()
            );
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String label,
            @NotNull final String[] args) {
        final List<String> emptyList = Collections.emptyList();
        if (args.length == 0) {
            return null;
        }

        final String name = args[0];

        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (final String string : this.sortedCommandNames) {
                if (string.startsWith(name.toLowerCase())) {
                    completions.add(string);
                }
            }

            return completions;
        }

        final BaseCommand baseCommand = this.commandMap.get(args[0]);

        if (baseCommand != null) {
            return baseCommand.getCompletions(this.getActualArgs(args));
        }

        return emptyList;
    }

    private String[] getActualArgs(final String[] args) {
        final String[] actualArgs = new String[args.length - 1];

        System.arraycopy(args, 1, actualArgs, 0, args.length - 1);

        return actualArgs;
    }

    public String getName() {
        return this.name;
    }
}
