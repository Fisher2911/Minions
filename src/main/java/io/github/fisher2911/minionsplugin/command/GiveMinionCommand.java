package io.github.fisher2911.minionsplugin.command;

import io.github.fisher2911.fishcore.message.Placeholder;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.lang.PlaceholderMessage;
import io.github.fisher2911.minionsplugin.minion.data.BaseMinionData;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.data.MinionDataManager;
import io.github.fisher2911.minionsplugin.world.MinionConverter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GiveMinionCommand extends BaseCommand {

    private final MinionDataManager minionDataManager;
    private final MinionConverter minionConverter;

    public GiveMinionCommand(final MinionsPlugin plugin) {
        super(plugin, "get", "minions.command.give", Set.of(Executor.PLAYER));
        this.minionDataManager = super.plugin.getMinionDataManager();
        this.minionConverter = super.plugin.getMinionConverter();
    }

    private static int fakeId = 0;

    @Override
    List<PlaceholderMessage> execute(final CommandSender sender, final String[] args) {
        final List<PlaceholderMessage> messages = new ArrayList<>();
        if (args.length != 1) {
            messages.add(PlaceholderMessage.withoutPlaceholders(Messages.REQUIRES_ID));
            return messages;
        }

        final Player player = (Player) sender;

        final Optional<BaseMinionData> optionalMinionData = this.minionDataManager.get(args[0]);

        if (optionalMinionData.isEmpty()) {
            messages.add(PlaceholderMessage.withoutPlaceholders(Messages.INVALID_ID));
            return messages;
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
            messages.add(PlaceholderMessage.withoutPlaceholders(Messages.ERROR_RECEIVING_ITEM));
            return messages;
        }

        final ItemStack itemStack = this.minionConverter.minionDataToItemStack(
                minionData);

        player.getInventory().addItem(itemStack);

        messages.add(new PlaceholderMessage(
                Messages.GIVEN_ITEM,
                Map.of(Placeholder.ITEM, itemStack.getType().toString())
        ));

        return messages;
    }

    @Override
    List<String> getCompletions(final String[] args) {
        final List<String> ids = this.minionDataManager.listIds();

        final List<String> tabs = new ArrayList<>();

        final String arg = args[1];

        if (args.length == 2) {

            for (final String id : ids) {
                if (id.startsWith(arg)) {
                    tabs.add(id);
                }
            }
            return tabs;
        }

        return null;
    }
}
