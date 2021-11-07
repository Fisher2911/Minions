package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.BaseGui;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;

public abstract class BaseMinionGui<G extends BaseGui> {

    public static final String MAIN = "main";

    protected final BaseMinion<?> minion;
    protected final MinionUser guiOwner;
    protected final GuiData guiData;

    public BaseMinionGui(
            final BaseMinion<?> minion,
            final MinionUser guiOwner,
            final GuiData guiData) {
        this.minion = minion;
        this.guiOwner = guiOwner;
        this.guiData = guiData;
    }

    public abstract G get();

    public abstract void updateItems();

    public GuiData getGuiData() {
        return this.guiData;
    }

    public BaseMinion<?> getMinion() {
        return this.minion;
    }

    public MinionUser getGuiOwner() {
        return this.guiOwner;
    }
}
