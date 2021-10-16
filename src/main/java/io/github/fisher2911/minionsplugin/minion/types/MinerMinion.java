package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public class MinerMinion extends BlockMinion {

    public MinerMinion(final JavaPlugin plugin,
                       final LocalDateTime lastActionTime,
                       final long id,
                       final UUID owner,
                       final Region region,
                       final MinionData minionData) {
        super(plugin, lastActionTime, id, owner, region, minionData);
    }

    @Override
    public void performAction(final Block block) {
        if (!this.isPlaced()) {
            return;
        }

        final ArmorStand minion = this.getMinion();

        final Block blockInFront = minion.getTargetBlock(null, 1);

        if (blockInFront.getType() == Material.AIR) {
            return;
        }

        final Collection<ItemStack> drops = blockInFront.getDrops();

        blockInFront.setType(Material.AIR);

        this.getInventory().addItem(drops.toArray(new ItemStack[0]));
        this.setLastActionTime(LocalDateTime.now());
    }
    
}
