package io.github.fisher2911.minionsplugin.minion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class MinionInventory {

    /**
     * The minions inventory that holds items.
     */
    private final Set<ItemStack> storedItemStacks;
    private final Armor armor;

    /**
     *
     * @param storedItemStacks items the minion has stored from actions
     * @param armor armor
     */

    public MinionInventory(final Set<ItemStack> storedItemStacks, final Armor armor) {
        this.storedItemStacks = storedItemStacks;
        this.armor = armor;
    }

    public Set<ItemStack> getStoredItemStacks() {
        return this.storedItemStacks;
    }

    public Armor getArmor() {
        return this.armor;
    }

    /**
     *
     * @param entity Entity whose armor is being set
     */
    public void setArmor(final LivingEntity entity) {
        this.armor.setArmor(entity);
    }
}
