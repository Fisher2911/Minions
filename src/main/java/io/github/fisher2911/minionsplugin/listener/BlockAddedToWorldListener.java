package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.message.MessageHandler;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.LeatherArmorBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.event.BlockChangedInWorldEvent;
import io.github.fisher2911.minionsplugin.minion.Armor;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.manager.MinionStorage;
import io.github.fisher2911.minionsplugin.minion.types.BlockMinion;
import io.github.fisher2911.minionsplugin.minion.types.FarmerMinion;
import io.github.fisher2911.minionsplugin.scheduler.MinionScheduler;
import io.github.fisher2911.minionsplugin.scheduler.MinionTaskData;
import io.github.fisher2911.minionsplugin.world.RectangularRegion;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.time.LocalDateTime;
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

        final Material material = block.getType();

        if (material == Material.END_PORTAL_FRAME) {

            final int y = block.getY();

            int totalMinions = 0;

            for (int x = -100; x <= 100; x++) {
                for (int z = -100; z <= 100; z++) {
                    final Position position = new Position(block.getWorld(),
                            x + block.getX(), y, z + block.getZ());
                    if (x % 5 == 0 && z % 5 == 0) {
                        position.setBlockType(Material.WATER);

                        final LeatherArmorBuilder builder =
                                LeatherArmorBuilder.from(Material.LEATHER_BOOTS).
                                        color(Color.RED);

                        final String name = MessageHandler.getInstance().
                                parseStringToString("<gradient:blue:green>Farmer Minion</gradient>");

                        final Position origin = position.add(0, 1, 0);

                        final BlockMinion baseMinion = new FarmerMinion(
                                this.plugin,
                                LocalDateTime.now(),
                                id++,
                                event.getPlayer().getUniqueId(),
                                new RectangularRegion(
                                        origin,
                                        origin.subtract(5, 1, 5),
                                        origin.add(5, 5, 5)
                                ),
                                new MinionData(
                                        new MinionInventory(
                                                Bukkit.createInventory(null, 9, name + "'s Inventory"),
                                                Armor.builder().
                                                        boots(builder.build()).
                                                        pants(LeatherArmorBuilder.
                                                                from(Material.LEATHER_LEGGINGS).
                                                                color(Color.BLUE).
                                                                build()).
                                                        chestPlate(LeatherArmorBuilder.
                                                                from(Material.LEATHER_CHESTPLATE).
                                                                color(Color.GREEN).
                                                                build()).
                                                        helmet(SkullBuilder.
                                                                create().
                                                                owner(Bukkit.getOfflinePlayer("NOTCH")).
                                                                build()).
                                                        mainHand(ItemBuilder.from(Material.DIAMOND_HOE).
                                                                glow(true).build()).
                                                        offHand(ItemBuilder.from(Material.WHEAT_SEEDS).
                                                                glow(true).build()).
                                                        build()
                                        ),
                                        name,
                                        0), Material.WHEAT);
                        baseMinion.place();
                        this.minionManager.addBlockMinion(baseMinion);
                        totalMinions++;
                        continue;
                    }
                    position.setBlockType(Material.DIRT);
                }
            }
            event.getPlayer().sendMessage("Total minions: " + totalMinions);
            return;
        }

        final Position position = Position.fromBukkitLocation(block.getLocation());

        final Optional<MinionStorage<BlockMinion>> optionalBlockMinionMinionPositions =
                this.
                        minionManager.
                        getBlockMinionsInChunk(
                                block.getWorld(),
                                position.getChunkKey()
                );

        final BlockChangedInWorldEvent blockChangedInWorldEvent = new BlockChangedInWorldEvent(block, BlockChangedInWorldEvent.Type.ADDED);

        optionalBlockMinionMinionPositions.ifPresent(chunkPositions ->
                chunkPositions.getMinionMap().values().forEach(
                minion -> {

                    if (minion.canPerformAction()) {
                        minion.performAction(blockChangedInWorldEvent);
                        return;
                    }

                    this.minionScheduler.addMinionTaskData(
                            new MinionTaskData<>(minion, blockChangedInWorldEvent)
                    );
                }));
    }

}
