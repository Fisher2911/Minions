package io.github.fisher2911.minionsplugin.command;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.data.BaseMinionData;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.data.MinionDataManager;
import io.github.fisher2911.minionsplugin.world.MinionConverter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestCommand implements CommandExecutor, TabCompleter {

    private final MinionsPlugin plugin;
    private final MinionDataManager minionDataManager;
    private final MinionConverter minionConverter;

    public TestCommand(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionDataManager = this.plugin.getMinionDataManager();
        this.minionConverter = this.plugin.getMinionConverter();
    }


    // todo - NOT ACTUAL IMPLEMENTATION : REMOVE
    private static long fakeId = 0;

    @Override
    public boolean onCommand(@NotNull final CommandSender sender,
                             @NotNull final Command command,
                             @NotNull final String label,
                             @NotNull final String[] args) {

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "You must specify an id!");
            return true;
        }

        if (!(sender instanceof final Player player)) {
            return true;
        }

        final Optional<BaseMinionData> optionalMinionData = this.minionDataManager.get(args[0]);

        if (optionalMinionData.isEmpty()) {
            player.sendMessage(ChatColor.RED + "That id does not exist!");
            return true;
        }

        final MinionData minionData = optionalMinionData.
                get().
                toMinionData(
                        fakeId++,
                        player.getUniqueId(),
                        100,
                        this.plugin
                );

        if (minionData == null) {
            player.sendMessage(ChatColor.RED + "Error getting minion item");
            return true;
        }

        final ItemStack itemStack = this.minionConverter.minionDataToItemStack(
                minionData);

        player.getInventory().addItem(itemStack);

        player.sendMessage(ChatColor.GREEN + "Received minion item");

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String alias,
            @NotNull final String[] args) {
        final List<String> ids = this.minionDataManager.listIds();

        if (args.length != 1) {
            return null;
        }

        final List<String> tabs = new ArrayList<>();

        final String arg = args[0];

        for (final String id : ids) {
            if (id.startsWith(arg)) {
                tabs.add(id);
            }
        }

        return tabs;
    }
}
