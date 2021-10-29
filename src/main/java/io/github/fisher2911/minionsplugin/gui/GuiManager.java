package io.github.fisher2911.minionsplugin.gui;

import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    private static final Map<String, BaseMinionGui<?>> minionGuiMap = new HashMap<>();

    public static <T extends BaseMinionGui<?>> void registerGui(final String id, final T minionGui) {
        minionGuiMap.put(id, minionGui);
    }

    public static BaseMinionGui<?> getGui(final String id) {
        return minionGuiMap.get(id);
    }

}
