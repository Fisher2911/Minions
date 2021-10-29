package io.github.fisher2911.minionsplugin.minion.types.data;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MinionFood {

    private final ItemStack itemStack;
    private final float foodValue;

    public MinionFood(final ItemStack itemStack, final float foodValue) {
        this.itemStack = itemStack;
        this.foodValue = foodValue;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public float getFoodValue() {
        return this.foodValue;
    }

    public boolean isFood(final ItemStack check) {
        return check.isSimilar(this.itemStack);
    }
}
