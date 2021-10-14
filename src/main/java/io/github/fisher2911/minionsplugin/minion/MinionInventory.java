package io.github.fisher2911.minionsplugin.minion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;

public class MinionInventory {

    private final Inventory inventory;
    private final Armor armor;

    /**
     *
     * @param inventory inventory
     * @param armor armor
     */

    public MinionInventory(final Inventory inventory, final Armor armor) {
        this.inventory = inventory;
        this.armor = armor;
    }

    public Inventory getInventory() {
        return this.inventory;
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
