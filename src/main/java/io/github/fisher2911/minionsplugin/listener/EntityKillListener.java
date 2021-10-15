package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class EntityKillListener implements Listener  {

    private final MinionsPlugin minionsPlugin;
    private final MinionManager minionManager;

    public EntityKillListener(final MinionsPlugin minionsPlugin) {
        this.minionsPlugin = minionsPlugin;
        this.minionManager = this.minionsPlugin.getMinionManager();
    }

    @EventHandler
    public void entityKillEvent(final EntityDeathEvent event) {

        final Entity killed = event.getEntity();

        final EntityDamageEvent lastDamageCause = killed.getLastDamageCause();

        if (lastDamageCause instanceof final EntityDamageByEntityEvent entityDamageByEntityEvent) {
            final Entity damager = entityDamageByEntityEvent.getDamager();

            if (damager instanceof final ArmorStand armorStand) {
                final Optional<BaseMinion> minionOptional = this.minionManager.getMinion(armorStand);

                minionOptional.ifPresent(minion -> {
                    final Inventory inventory = minion.getInventory();
                    inventory.addItem(event.getDrops().toArray(new ItemStack[0]));
                    event.getDrops().clear();
                });
            }
        }


    }
}
