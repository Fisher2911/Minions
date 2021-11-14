package io.github.fisher2911.minionsplugin.gui;

import dev.triumphteam.gui.guis.BaseGui;
import io.github.fisher2911.minionsplugin.gui.item.TypeItem;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMinionGui<G extends BaseGui> {

    public static final String MAIN = "main";
    public static final String PREVIOUS_PAGE = "previous";
    public static final String NEXT_PAGE = "next";

    protected final BaseMinion<?> minion;
    protected final MinionUser guiOwner;
    protected final GuiData guiData;
    protected final String extraData;

    public BaseMinionGui(
            final BaseMinion<?> minion,
            final MinionUser guiOwner,
            final GuiData guiData,
            final String extraData) {
        this.minion = minion;
        this.guiOwner = guiOwner;
        this.guiData = guiData;
        this.extraData = extraData;
    }

    public abstract G get();

    public abstract void updateItems();

    @Nullable
    public abstract G nextPage();

    @Nullable
    public abstract G previousPage();

    public abstract void openNextPage(final HumanEntity player);

    public abstract void openPreviousPage(final HumanEntity player);

    public GuiData getGuiData() {
        return this.guiData;
    }

    public BaseMinion<?> getMinion() {
        return this.minion;
    }

    public MinionUser getGuiOwner() {
        return this.guiOwner;
    }

    public String getExtraData() {
        return this.extraData;
    }

    @Nullable
    public abstract TypeItem getItemAtSlot(final int slot, final boolean accountForPage);
}
