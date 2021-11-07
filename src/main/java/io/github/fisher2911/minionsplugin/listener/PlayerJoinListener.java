package io.github.fisher2911.minionsplugin.listener;

import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;

public class PlayerJoinListener implements Listener {

    private final MinionsPlugin plugin;
    private final UserManager<MinionUser> userManager;

    public PlayerJoinListener(final MinionsPlugin plugin) {
        this.plugin = plugin;
        this.userManager = this.plugin.getUserManager();
    }

    // todo - actual loading, this is just for testing
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        this.userManager.add(new MinionUser(
                player.getUniqueId(),
                player.getName(),
                new HashSet<>()
        ));
    }

    // todo - actual saving, this is just for testing
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.userManager.remove(player.getUniqueId());
    }
}
