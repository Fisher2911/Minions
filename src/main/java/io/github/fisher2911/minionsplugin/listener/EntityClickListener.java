package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.minion.food.FeedResponse;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class EntityClickListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionManager minionManager;
    private final UserManager<MinionUser> userManager;
    private final GuiManager guiManager;

    public EntityClickListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
        this.userManager = this.plugin.getUserManager();
        this.guiManager = this.plugin.getGuiManager();
    }

    @EventHandler
    public void onClick(final PlayerInteractAtEntityEvent event) {
        final Entity interactedWith = event.getRightClicked();

        if (interactedWith instanceof final ArmorStand armorStand) {

            final Optional<? extends BaseMinion<?>> minionOptional = this.minionManager.getBaseMinion(armorStand);


            event.setCancelled(true);

            final Player player = event.getPlayer();

            final Optional<MinionUser> optionalMinionUser =
                    this.userManager.get(player.getUniqueId());

            if (optionalMinionUser.isEmpty()) {
                return;
            }

            final MinionUser user = optionalMinionUser.get();

            minionOptional.ifPresentOrElse(minion -> {
                event.getPlayer().sendMessage("Opened");

                final ItemStack clickedWith = event.getPlayer().getInventory().getItemInMainHand();

                if (clickedWith.getType() != Material.AIR &&
                        minion.feed(user, clickedWith) != FeedResponse.CANNOT_FEED) {
                    return;
                }

                this.guiManager.
                        openMinionGui(BaseMinionGui.MAIN,
                                user,
                                minion
                        );

//                event.getPlayer().openInventory(minion.getInventory());
//                        event.setCancelled(true);
            }, () -> {
                player.sendMessage("Minion not found");
            });
        }
    }
}
