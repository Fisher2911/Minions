package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.EntityMinion;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
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

                final Optional<EntityMinion> minionOptional =
                        this.minionManager.getEntityMinion(armorStand);

                minionOptional.ifPresent(minion -> {
                    final MinionInventory inventory = minion.getInventory();
                    inventory.addStoredItemStack(event.getDrops().toArray(new ItemStack[0]));
                    event.getDrops().clear();
                });
            }
        }


    }
}
