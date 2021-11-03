package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.manager.MinionStorage;
import io.github.fisher2911.minionsplugin.minion.types.BlockMinion;
import io.github.fisher2911.minionsplugin.scheduler.MinionScheduler;
import io.github.fisher2911.minionsplugin.scheduler.MinionTaskData;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

public class BlockAddedToWorldListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionManager minionManager;
    private final MinionScheduler<BlockMinion, BlockChangedInWorldEvent> minionScheduler;

    public BlockAddedToWorldListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
        this.minionScheduler = new MinionScheduler<>(this.plugin);
        this.minionScheduler.start();
    }

    private static long id = 0;

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();

        final Position position = Position.fromBukkitLocation(block.getLocation());

        final BlockChangedInWorldEvent blockChangedInWorldEvent = new BlockChangedInWorldEvent(block, BlockChangedInWorldEvent.Type.ADDED);

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                final Optional<MinionStorage<BlockMinion>> optionalBlockMinionMinionPositions =
                        this.
                                minionManager.
                                getBlockMinionsInChunk(
                                        block.getWorld(),
                                        position.add(x * 16, 0, z * 16).getChunkKey()
                                );

                optionalBlockMinionMinionPositions.ifPresent(chunkPositions ->
                        chunkPositions.getMinionMap().values().forEach(
                                minion -> {

                                    if (minion.canPerformAction()) {
                                        minion.performAction(blockChangedInWorldEvent);
                                        return;
                                    }

                                    if (!minion.getRegion().contains(position)) {
                                        return;
                                    }

                                    this.minionScheduler.addMinionTaskData(
                                            new MinionTaskData<>(minion, blockChangedInWorldEvent)
                                    );
                                }));
            }
        }
    }

}
