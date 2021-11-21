package io.github.fisher2911.minionsplugin.command;

import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.lang.PlaceholderMessage;
import io.github.fisher2911.minionsplugin.permission.MinionPermissions;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.permission.PermissionManager;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PermissionsGroupCommand extends BaseCommand {

    private final UserManager<MinionUser> userManager;

    public PermissionsGroupCommand(final MinionsPlugin plugin) {
        super(plugin, "group",
                "minions.command.permissions.group.create",
                Set.of(Executor.PLAYER));
        this.userManager = super.plugin.getUserManager();
    }

    @Override
    List<PlaceholderMessage> execute(final CommandSender sender, final String[] args) {
        final List<PlaceholderMessage> messages = new ArrayList<>();
        final String arg = args[0];

        final Player player = (Player) sender;

        final Optional<MinionUser> userOptional = this.userManager.get(player.getUniqueId());

        if (userOptional.isEmpty()) {
            player.sendMessage("Invalid user");
            return messages;
        }

        final MinionUser user = userOptional.get();

        if (arg.equalsIgnoreCase("create")) {

            if (args.length < 2) {
                messages.add(new PlaceholderMessage(
                        Messages.MISSING_ARGUMENT,
                        Map.of(Messages.ARGUMENT, "id")
                ));
                return messages;
            }

            if (args.length < 3) {
                messages.add(new PlaceholderMessage(
                        Messages.MISSING_ARGUMENT,
                        Map.of(Messages.ARGUMENT, "ranking")
                ));

                return messages;
            }

            if (args.length < 4) {
                messages.add(new PlaceholderMessage(
                        Messages.MISSING_ARGUMENT,
                        Map.of(Messages.ARGUMENT, "name")
                ));

                return messages;
            }

            final String id = args[1];
            try {
                final int ranking = Integer.parseInt(args[2]);

                if (ranking < 0) {
                    throw new IllegalArgumentException();
                }

                final String name = args[2];

                final ItemStack itemStack = player.getInventory().getItemInMainHand();

                if (itemStack.getType() == Material.AIR) {
                    messages.add(
                            PlaceholderMessage.withoutPlaceholders(
                                    Messages.MUST_BE_HOLDING_ITEM
                            )
                    );

                    return messages;
                }

                final MinionPermissionsGroup permissionsGroup =
                        new MinionPermissionsGroup(
                                id,
                                ranking,
                                name,
                                new TypeItem(
                                        TypeItem.Types.PERMISSION_GROUP,
                                        id,
                                        itemStack),
                                MinionPermissionsGroup.Mode.ALL,
                                new HashSet<>(),
                                new ArrayList<>(),
                                new MinionPermissions(
                                        PermissionManager.getInstance().getDefaultPermissions()
                                )
                        );

                user.addMinionPermissionsGroup(
                       permissionsGroup
                );

                messages.add(new PlaceholderMessage(
                        Messages.CREATED_PERMISSION_GROUP,
                        Map.of(Messages.INPUT, id)
                ));

            } catch (final IllegalArgumentException exception) {
                messages.add(new PlaceholderMessage(
                        Messages.INVALID_ARGUMENT,
                        Map.of(Messages.ARGUMENT, "ranking",
                                Messages.INPUT, args[2])
                ));
                return messages;
            }

        }

        return messages;
    }

    @Override
    List<String> getCompletions(final String[] args) {
        return null;
    }
}
