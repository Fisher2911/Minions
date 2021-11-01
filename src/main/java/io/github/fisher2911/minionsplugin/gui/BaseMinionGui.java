package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.BaseGui;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;

public abstract class BaseMinionGui<G extends BaseGui> {

    public static final String MAIN = "main";

    protected final BaseMinion<?> baseMinion;
    protected final MinionUser guiOwner;
    protected final GuiData guiData;

    public BaseMinionGui(
            final BaseMinion<?> baseMinion,
            final MinionUser guiOwner,
            final GuiData guiData) {
        this.baseMinion = baseMinion;
        this.guiOwner = guiOwner;
        this.guiData = guiData;
    }

    public abstract G create();

    public GuiData getGuiData() {
        return this.guiData;
    }

    public BaseMinion<?> getMinion() {
        return this.baseMinion;
    }

    public MinionUser getGuiOwner() {
        return this.guiOwner;
    }
}
