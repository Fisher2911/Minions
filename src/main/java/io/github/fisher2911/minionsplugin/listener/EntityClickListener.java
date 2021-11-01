package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

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

            minionOptional.ifPresentOrElse(minion -> {
                event.getPlayer().sendMessage("Opened");

                this.guiManager.
                        openMinionGui(BaseMinionGui.MAIN,
                                optionalMinionUser.get(),
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
