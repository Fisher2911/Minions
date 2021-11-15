package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeGroupManager;
import io.github.fisher2911.minionsplugin.world.MinionConverter;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Optional;

public class MinionPlaceListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionConverter minionConverter;
    private final MinionManager minionManager;
    private final UpgradeGroupManager upgradeGroupManager;

    public MinionPlaceListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionConverter = this.plugin.getMinionConverter();
        this.minionManager = this.plugin.getMinionManager();
        this.upgradeGroupManager = this.plugin.getUpgradeGroupManager();
    }

    @EventHandler
    public void onRightClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemStack heldItem = event.getItem();

        if (heldItem == null) {
            return;
        }

        final Position origin = Position.fromBukkitLocation(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));

        final Optional<BaseMinion<?>> minionOptional = this.minionConverter.itemStackToMinion(
                heldItem,
                origin
        );

        minionOptional.ifPresentOrElse(minion -> {

//            minion.getMinionData().getMinionPermissionsGroup(PermissionManager.getInstance().getDefaultGroupId()).
//                    getMembers().
//                    add(player.getUniqueId());

            minion.place();
            this.minionManager.addMinion(minion);
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "Placed minion");

            final PlayerInventory inventory = player.getInventory();
            final ItemStack itemStack = inventory.getItemInMainHand().clone();
            itemStack.setAmount(itemStack.getAmount() - 1);
            inventory.setItemInMainHand(itemStack);

        }, () -> player.sendMessage("No minion"));
    }
}
