package io.github.fisher2911.minionsplugin.gui;

import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.user.MinionUser;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    private final Map<String, GuiData> minionGuiMap = new HashMap<>();

    public <T extends BaseMinionGui<?>> void registerGui(final String id, final GuiData guiData) {
        this.minionGuiMap.put(id, guiData);
    }

    public @Nullable GuiData getGuiData(final String id) {
        return this.minionGuiMap.get(id);
    }

    public @Nullable SimpleMinionGui getGui(
            final String id,
            final MinionUser minionUser,
            final BaseMinion<?> minion) {
        final GuiData guiData = this.getGuiData(id);

        if (guiData == null) {
            return null;
        }

        return new SimpleMinionGui(minion, minionUser, guiData);
    }

    public void openMinionGui(
            final String id,
            final MinionUser minionUser,
            final BaseMinion<?> minion) {
        final SimpleMinionGui gui = this.getGui(id, minionUser, minion);

        if (gui == null) {
            return;
        }

        minionUser.ifOnline(gui.create()::open);
    }

}
