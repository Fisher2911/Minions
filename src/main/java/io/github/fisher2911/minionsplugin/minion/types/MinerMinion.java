package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public class MinerMinion extends BlockMinion {

    public MinerMinion(final @NotNull JavaPlugin plugin,
                       final @NotNull LocalDateTime lastActionTime,
                       final long id,
                       final @NotNull UUID owner,
                       final @NotNull Region region,
                       final @NotNull MinionData minionData,
                       final @NotNull Upgrades upgrades) {
        super(plugin, lastActionTime, id, owner, region, minionData, upgrades);
    }

    @Override
    public boolean performAction(final @NotNull BlockChangedInWorldEvent event) {
        if (!this.isPlaced()) {
            return true;
        }

        if (!this.canPerformAction()) {
            return false;
        }

        final ArmorStand minion = this.getMinion();

        final Block blockInFront = minion.getTargetBlock(null, 1);

        if (blockInFront.getType() == Material.AIR) {
            return true;
        }

        final Collection<ItemStack> drops = blockInFront.getDrops();

        blockInFront.setType(Material.AIR);

        this.getInventory().addItem(drops.toArray(new ItemStack[0]));
        this.setLastActionTime(LocalDateTime.now());
        return true;
    }
    
}
