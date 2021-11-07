package io.github.fisher2911.minionsplugin.minion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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

    /**
     *
     * @param itemStack {@link org.bukkit.inventory.ItemStack} to be added to stored items
     */
    public void addStoredItemStack(final ItemStack... itemStack) {
        this.storedItemStacks.addAll(List.of(itemStack));
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
