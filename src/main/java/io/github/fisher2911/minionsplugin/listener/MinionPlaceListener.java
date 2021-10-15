package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.message.MessageHandler;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.fishcore.util.builder.LeatherArmorBuilder;
import io.github.fisher2911.fishcore.util.builder.SkullBuilder;
import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.minion.Armor;
import io.github.fisher2911.minionsplugin.minion.MinionData;
import io.github.fisher2911.minionsplugin.minion.MinionInventory;
import io.github.fisher2911.minionsplugin.minion.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.minion.types.MinerMinion;
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
                        color(Color.AQUA);

        final BaseMinion baseMinion = new MinerMinion(
                this.plugin,
                id++,
                player.getUniqueId(),
                Position.fromBukkitLocation(event.getClickedBlock().getLocation().add(0.5, 1, 0.5)),
                new MinionData(
                        new MinionInventory(
                                Bukkit.createInventory(null, 9, "test"),
                                Armor.builder().
                                        boots(builder.build()).
                                        pants(LeatherArmorBuilder.
                                                from(Material.LEATHER_LEGGINGS).
                                                color(Color.AQUA).
                                                build()).
                                        chestPlate(LeatherArmorBuilder.
                                        from(Material.LEATHER_CHESTPLATE).
                                        color(Color.AQUA).
                                        build()).
                                        helmet(SkullBuilder.
                                                create().
                                                owner(Bukkit.getOfflinePlayer("Notch")).
                                                build()).
                                        mainHand(ItemBuilder.from(Material.DIAMOND_AXE).
                                                glow(true).build()).
                                        offHand(ItemBuilder.from(Material.OAK_LOG).
                                                glow(true).build()).
                                        build()
                        ),
                        MessageHandler.getInstance().
                                parseStringToString("<gradient:blue:green>WoodCutter Minion</gradient>!"),
                        0));
        baseMinion.place();
        this.minionManager.addMinion(baseMinion);
        event.setCancelled(true);
    }
}
