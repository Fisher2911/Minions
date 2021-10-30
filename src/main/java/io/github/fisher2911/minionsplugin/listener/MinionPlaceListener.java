package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.LeatherArmorBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.fishcore.util.helper.StringUtils;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.Armor;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BlockMinion;
import io.github.fisher2911.minionsplugin.minion.types.FarmerMinion;
import io.github.fisher2911.minionsplugin.minion.types.data.MinionData;
import io.github.fisher2911.minionsplugin.upgrade.SpeedUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.UpgradeData;
import io.github.fisher2911.minionsplugin.upgrade.Upgrades;
import io.github.fisher2911.minionsplugin.world.RectangularRegion;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

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
                LocalDateTime.now(),
                id++,
                player.getUniqueId(),
                new RectangularRegion(
                        origin,
                        origin.subtract(5, 1, 5),
                        origin.add(5, 5, 5)
                ),
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
                        name,
                        0), Material.WHEAT, new Upgrades(new UpgradeData<>(0,
                new SpeedUpgrade("test", "test", new HashMap<>(),
                        new HashMap<>(), new ItemStack(Material.ITEM_FRAME)))));
        baseMinion.place();
        this.minionManager.addBlockMinion(baseMinion);
        event.setCancelled(true);
    }
}
