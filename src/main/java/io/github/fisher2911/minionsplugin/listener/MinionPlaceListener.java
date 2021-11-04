package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.LeatherArmorBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.fishcore.util.helper.StringUtils;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.minion.Armor;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import io.github.fisher2911.minionsplugin.minion.food.FoodData;
import io.github.fisher2911.minionsplugin.minion.food.FoodGroup;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BlockMinion;
import io.github.fisher2911.minionsplugin.minion.types.FarmerMinion;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.FoodPerActionUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.RangeUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.SpeedUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeData;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeType;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.Range;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MinionPlaceListener implements Listener {

    private final MinionsPlugin plugin;
    private final MinionManager minionManager;

    private static int id = 0;

    public MinionPlaceListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.minionManager = this.plugin.getMinionManager();
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

        if (heldItem == null || heldItem.getType() != Material.ARMOR_STAND) {
            return;
        }

        final LeatherArmorBuilder builder =
                LeatherArmorBuilder.from(Material.LEATHER_BOOTS).
                        color(Color.RED);

        final String name = StringUtils.
                parseStringToString("<gradient:blue:green>Farmer Minion</gradient>");

        final Position origin = Position.fromBukkitLocation(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));

        final BlockMinion baseMinion = new FarmerMinion(
                this.plugin,
                Instant.now(),
                id++,
                player.getUniqueId(),
                MinionType.BLOCK,
                origin,
                new MinionData(
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
                        name
                ), Material.WHEAT, new Upgrades(new UpgradeData<>(1,
                new SpeedUpgrade("test",
                        "test",
                        Map.of(1, 5f, 2, 1f),
                        Map.of(1, new Cost(0, new ArrayList<>()),
                                2, new Cost(5, new ArrayList<>())),
                        ItemBuilder.from(Material.ITEM_FRAME).
                                name(StringUtils.parseStringToString(
                                        "<blue>" + "Level: " + Placeholder.LEVEL)).
                                lore(List.of("",
                                        StringUtils.parseStringToString(
                                                "<green>" + "Cost: " + Placeholder.MONEY_COST))).
                                build(),
                        UpgradeType.SPEED_UPGRADE)),
                new UpgradeData<>(1,
                        new RangeUpgrade("test",
                                "test",
                                Map.of(1, new Range(
                                        2, 2, 2, 2, 2, 2),
                                        2, new Range(
                                                5, 5, 5, 5, 5, 5
                                        )),
                                Map.of(1, new Cost(0, new ArrayList<>()),
                                        2, new Cost(5, new ArrayList<>())),
                                ItemBuilder.from(Material.BOW).
                                        name(StringUtils.parseStringToString(
                                                "<blue>" + "Level: " + Placeholder.LEVEL)).
                                        lore(List.of("",
                                                StringUtils.parseStringToString(
                                                        "<green>" + "Cost: " + Placeholder.MONEY_COST))).
                                        build(),
                                UpgradeType.RANGE_UPGRADE)),
                new UpgradeData<>(1, new FoodPerActionUpgrade(
                        "test",
                        "test",
                        Map.of(1, 2f,
                                2, 1f),
                        Map.of(1, new Cost(0, new ArrayList<>()),
                                2, new Cost(5, new ArrayList<>())),
                        ItemBuilder.from(Material.COOKED_CHICKEN).
                                name(StringUtils.parseStringToString(
                                        "<blue>" + "Level: " + Placeholder.LEVEL)).
                                lore(List.of("",
                                        StringUtils.parseStringToString(
                                                "<green>" + "Cost: " + Placeholder.MONEY_COST))).
                                build(),
                        UpgradeType.FOOD_PER_ACTION)
                )));
        baseMinion.place();
        this.minionManager.addBlockMinion(baseMinion);
        event.setCancelled(true);
    }
}
