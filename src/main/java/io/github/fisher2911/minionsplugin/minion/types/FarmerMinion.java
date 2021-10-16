package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.world.Region;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.UUID;

public class FarmerMinion extends BlockMinion {

    public FarmerMinion(final JavaPlugin plugin,
                        final LocalDateTime lastActionTime,
                        final long id, final UUID owner,
                        final Region region,
                        final MinionData minionData) {
        super(plugin, lastActionTime, id, owner, region, minionData);
    }

    @Override
    public void performAction(final Block block) {
        
    }
}
