package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public class MinerMinion extends BlockMinion {

    public MinerMinion(final JavaPlugin plugin,
                       final Instant lastActionTime,
                       final long id,
                       final UUID owner,
                       final MinionType minionType,
                       final Position position,
                       final MinionData minionData) {
        super(plugin, lastActionTime, id, owner, minionType, position, minionData);
    }

    @Override
    public ActionResult performAction(final BlockChangedInWorldEvent event) {
        final ArmorStand minion = this.getMinion();

        final Block blockInFront = minion.getTargetBlock(null, 1);

        if (blockInFront.getType() == Material.AIR) {
            return ActionResult.FAIL;
        }

        final Collection<ItemStack> drops = blockInFront.getDrops();

        blockInFront.setType(Material.AIR);

        this.getInventory().addStoredItemStack(drops.toArray(new ItemStack[0]));
        this.setLastActionTime(Instant.now());
        return ActionResult.SUCCESS;
    }
    
}
