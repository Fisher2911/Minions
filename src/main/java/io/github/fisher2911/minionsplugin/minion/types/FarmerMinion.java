package io.github.fisher2911.minionsplugin.minion.types;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class FarmerMinion extends BaseMinion {

    public FarmerMinion(final JavaPlugin plugin, final long id, final UUID owner, final Position position, final MinionData minionData) {
        super(plugin, id, owner, position, minionData);
    }

    @Override
    public void performAction() {
        
    }
}
