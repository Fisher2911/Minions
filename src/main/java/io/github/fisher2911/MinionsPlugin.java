package io.github.fisher2911;


import io.github.fisher2911.fishcore.FishCore;
import io.github.fisher2911.fishcore.user.UserManager;
import io.github.fisher2911.minion.MinionManager;
import io.github.fisher2911.minion.types.BaseMinion;
import io.github.fisher2911.user.MinionUser;

public class MinionsPlugin extends FishCore {

    private UserManager<MinionUser> userManager;
    private MinionManager<BaseMinion> minionManager;

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
    }

    public UserManager<MinionUser> getUserManager() {
        return this.userManager;
    }

    public MinionManager<BaseMinion> getMinionManager() {
        return this.minionManager;
    }
}
