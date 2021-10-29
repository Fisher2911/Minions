package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.gui.BaseMinionGui;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
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

    public EntityClickListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
    }

    @EventHandler
    public void onClick(final PlayerInteractAtEntityEvent event) {
        final Entity interactedWith = event.getRightClicked();

        if (interactedWith instanceof final ArmorStand armorStand) {

            final Optional<? extends BaseMinion<?>> minionOptional = this.minionManager.getBaseMinion(armorStand);

            GuiManager.getGui(BaseMinionGui.MAIN).create().open(event.getPlayer());
            event.getPlayer().sendMessage("Opened");

            event.setCancelled(true);

            final Player player = event.getPlayer();

            minionOptional.ifPresentOrElse(minion -> {
                    player.sendMessage("Found Minion");
//                event.getPlayer().openInventory(minion.getInventory());
//                        event.setCancelled(true);
            }, () -> {
                player.sendMessage("Minion not found");
            });
        }
    }
}
