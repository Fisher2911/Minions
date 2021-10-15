package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.UUID;

public class MinerMinion extends BaseMinion {

    public MinerMinion(final JavaPlugin plugin, final long id, final UUID owner, final Position position, final MinionData minionData) {
        super(plugin, id, owner, position, minionData);
    }

    @Override
    public void performAction() {
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
    }
    
}
