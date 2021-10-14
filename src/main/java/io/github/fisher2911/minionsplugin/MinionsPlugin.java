package io.github.fisher2911.minionsplugin;


import io.github.fisher2911.fishcore.FishCore;
import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minionsplugin.listener.EntityClickListener;
import io.github.fisher2911.minionsplugin.listener.EntityKillListener;
import io.github.fisher2911.minionsplugin.minion.MinionManager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import io.github.fisher2911.minionsplugin.listener.MinionPlaceListener;
import io.github.fisher2911.minionsplugin.minion.MinionScheduler;

import java.util.List;

public class MinionsPlugin extends FishCore {

    private UserManager<MinionUser> userManager;
    private MinionManager<BaseMinion> minionManager;
    private MinionScheduler minionScheduler;

    @Override
    public void onEnable() {
        super.onEnable();
        this.initializeClasses();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void initializeClasses() {
        this.userManager = new UserManager<>();
        this.minionManager = new MinionManager<>();
        this.minionScheduler = new MinionScheduler(this);
        this.minionScheduler.start();

        List.of(new MinionPlaceListener(this),
                new EntityKillListener(this),
                new EntityClickListener(this)).
                forEach(this::registerListener);

    }

    public UserManager<MinionUser> getUserManager() {
        return this.userManager;
    }

    public MinionManager<BaseMinion> getMinionManager() {
        return this.minionManager;
    }
}
