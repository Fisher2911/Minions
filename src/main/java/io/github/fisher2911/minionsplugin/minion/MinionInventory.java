package io.github.fisher2911.minionsplugin.minion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class MinionInventory {

    private final Inventory inventory;
    private final Armor armor;

    /**
     *
     * @param inventory inventory
     * @param armor armor
     */

    public MinionInventory(final @NotNull Inventory inventory, final @NotNull Armor armor) {
        this.inventory = inventory;
        this.armor = armor;
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public @NotNull Armor getArmor() {
        return this.armor;
    }

    /**
     *
     * @param entity Entity whose armor is being set
     */



    public void setArmor(final @NotNull LivingEntity entity) {
        this.armor.setArmor(entity);
    }
}
