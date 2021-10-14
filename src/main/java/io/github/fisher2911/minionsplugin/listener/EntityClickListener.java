package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Optional;

public class EntityClickListener implements Listener {

    private final MinionsPlugin  plugin;
    private final MinionManager<BaseMinion> minionManager;

    public EntityClickListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
    }

    @EventHandler
    public void onClick(final PlayerInteractAtEntityEvent event) {
        final Entity interactedWith = event.getRightClicked();

        if (interactedWith instanceof final ArmorStand armorStand) {
            final long id = this.minionManager.getId(armorStand);

            if (id == -1) {
                return;
            }

            final Optional<BaseMinion> minionOptional = this.minionManager.get(id);

            minionOptional.ifPresent(minion -> {
                event.getPlayer().openInventory(minion.getInventory());
                        event.setCancelled(true);
            });
        }
    }
}