package io.github.fisher2911.minionsplugin;


import io.github.fisher2911.fishcore.FishCore;
import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.listener.BlockAddedToWorldListener;
import io.github.fisher2911.minionsplugin.listener.ChunkListener;
import io.github.fisher2911.minionsplugin.listener.EntityClickListener;
import io.github.fisher2911.minionsplugin.listener.EntityKillListener;
import io.github.fisher2911.minionsplugin.listener.MinionPlaceListener;
import io.github.fisher2911.minionsplugin.minion.manager.MinionManager;
import io.github.fisher2911.minionsplugin.user.MinionUser;

import java.util.List;

public class MinionsPlugin extends FishCore {

    private UserManager<MinionUser> userManager;
    private MinionManager minionManager;

    @Override
    public void onEnable() {
        super.onEnable();
        this.initializeClasses();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void initializeClasses() {
        this.userManager = new UserManager<>();
        this.minionManager = new MinionManager(this);
    }

    private void registerListeners() {
        List.of(new MinionPlaceListener(this),
                        new BlockAddedToWorldListener(this),
                        new EntityKillListener(this),
                        new EntityClickListener(this),
                        new ChunkListener(this)).
                forEach(this::registerListener);
    }

    public UserManager<MinionUser> getUserManager() {
        return this.userManager;
    }

    public MinionManager getMinionManager() {
        return this.minionManager;
    }
}
