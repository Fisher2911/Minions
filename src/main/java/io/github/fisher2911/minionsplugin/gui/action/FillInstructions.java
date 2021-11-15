package io.github.fisher2911.minionsplugin.gui.action;

import io.github.fisher2911.minionsplugin.gui.item.FillItem;

public class FillInstructions {

    private final String type;
    private final FillItem fillItem;

    public FillInstructions(final String type, final FillItem fillItem) {
        this.type = type;
        this.fillItem = fillItem;
    }

    public String getType() {
        return this.type;
    }

    public FillItem getFillItem() {
        return this.fillItem;
    }
}
