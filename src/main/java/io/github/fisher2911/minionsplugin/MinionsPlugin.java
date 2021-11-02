package io.github.fisher2911.minionsplugin;

import io.github.fisher2911.fishcore.FishCore;
import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.config.GuiLoader;
import io.github.fisher2911.minionsplugin.gui.GuiManager;
import io.github.fisher2911.minionsplugin.listener.BlockAddedToWorldListener;
import io.github.fisher2911.minionsplugin.listener.ChunkListener;
import io.github.fisher2911.minionsplugin.listener.EntityClickListener;
import io.github.fisher2911.minionsplugin.listener.EntityKillListener;
import io.github.fisher2911.minionsplugin.listener.MinionPlaceListener;
import io.github.fisher2911.minionsplugin.listener.PlayerJoinListener;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.permission.PermissionManager;
import io.github.fisher2911.minionsplugin.user.MinionUser;

import java.util.List;

public class MinionsPlugin extends FishCore {

    private UserManager<MinionUser> userManager;
    private PermissionManager permissionManager;
    private GuiManager guiManager;
    private MinionManager minionManager;

    @Override
    public void onEnable() {
        super.onEnable();
        this.initializeClasses();
        this.registerListeners();
        this.test();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void initializeClasses() {
        this.permissionManager = new PermissionManager();
        this.userManager = new UserManager<>();
        this.minionManager = new MinionManager(this);
        this.guiManager = new GuiManager();
    }

    private void registerListeners() {
        List.of(new MinionPlaceListener(this),
                        new BlockAddedToWorldListener(this),
                        new EntityKillListener(this),
                        new EntityClickListener(this),
                        new ChunkListener(this),
                        new PlayerJoinListener(this)).
                forEach(this::registerListener);
    }

    public PermissionManager getPermissionManager() {
        return this.permissionManager;
    }

    public UserManager<MinionUser> getUserManager() {
        return this.userManager;
    }

    public MinionManager getMinionManager() {
        return this.minionManager;
    }

    private void test() {
        final GuiLoader guiLoader = new GuiLoader(this);
        guiLoader.load("menus", "main.yml");
        guiLoader.load("menus", "permissions.yml");
        guiLoader.load("menus", "upgrades.yml");
        guiLoader.load("menus", "cosmetics.yml");
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }
}
