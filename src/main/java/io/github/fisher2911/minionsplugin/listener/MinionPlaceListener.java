package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.LeatherArmorBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.fishcore.util.helper.StringUtils;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.Armor;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.minion.food.FoodGroup;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.minion.types.BlockMinion;
import io.github.fisher2911.minionsplugin.minion.types.MinerMinion;
import io.github.fisher2911.minionsplugin.permission.MinionPermissions;
import io.github.fisher2911.minionsplugin.permission.MinionPermissionsGroup;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeGroupManager;
import io.github.fisher2911.minionsplugin.world.MinionConverter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

public class MinionPlaceListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionConverter minionConverter;
    private final MinionManager minionManager;
    private final UpgradeGroupManager upgradeGroupManager;

    private static int id = 0;

    public MinionPlaceListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionConverter = this.plugin.getMinionConverter();
        this.minionManager = this.plugin.getMinionManager();
        this.upgradeGroupManager = this.plugin.getUpgradeGroupManager();
    }

    @EventHandler
    public void onRightClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemStack heldItem = event.getItem();

        if (heldItem == null) {
            return;
        }

        final Position origin = Position.fromBukkitLocation(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));

        final Optional<BaseMinion<?>> minionOptional = this.minionConverter.itemStackToMinion(
                heldItem,
                origin
                );

        minionOptional.ifPresent(minion -> {
            minion.place();
            this.minionManager.addMinion(minion);
            event.setCancelled(true);
        });


        final LeatherArmorBuilder builder =
                LeatherArmorBuilder.from(Material.LEATHER_BOOTS).
                        color(Color.RED);

        final String name = StringUtils.
                parseStringToString("<gradient:blue:green>Miner Minion</gradient>");

        final BlockMinion baseMinion = new MinerMinion(
                this.plugin,
                Instant.now(),
                origin,
                new MinionData(
                        id++,
                        player.getUniqueId(),
                        MinionType.BLOCK,
                        new MinionPermissionsGroup(
                                1,
                                "test",
                                MinionPermissionsGroup.Mode.NOT_SPECIFIED,
                                new HashSet<>(),
                                new ArrayList<>(),
                                new MinionPermissions(new HashMap<>())
                        ),
                        new MinionInventory(
                        new HashSet<>(),
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
                        new FoodData(
                                new FoodGroup(
                                     Map.of(Material.COOKED_CHICKEN, 1f)
                                ),
                                50
                        ),
                        this.upgradeGroupManager.get("cobble-miner").get().toUpgrades(),
                        name
                ));
    }
}
