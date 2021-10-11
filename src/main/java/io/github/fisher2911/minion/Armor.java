package io.github.fisher2911.minion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Armor {

    private ItemStack boots;
    private ItemStack pants;
    private ItemStack chestPlate;
    private ItemStack helmet;

    public static Builder builder() {
        return new Builder();
    }

    private Armor(final ItemStack boots, final ItemStack pants, final ItemStack chestPlate, final ItemStack helmet) {
        this.boots = boots;
        this.pants = pants;
        this.chestPlate = chestPlate;
        this.helmet = helmet;
    }

    public ItemStack getBoots() {
        return this.boots;
    }

    public void setBoots(final ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack getPants() {
        return this.pants;
    }

    public void setPants(final ItemStack pants) {
        this.pants = pants;
    }

    public ItemStack getChestPlate() {
        return this.chestPlate;
    }

    public void setChestPlate(final ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    public ItemStack getHelmet() {
        return this.helmet;
    }

    public void setHelmet(final ItemStack helmet) {
        this.helmet = helmet;
    }

    /**
     *
     * @return converts ItemStacks to array for use with EntityEquipment
     */

    public ItemStack[] toArray() {
        return new ItemStack[]{this.boots, this.pants, this.chestPlate, this.helmet};
    }

    /**
     *
     * @param entity Entity whose armor is being set
     */


    public void setArmor(final LivingEntity entity) {
        final EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) {
            return;
        }
        equipment.setArmorContents(this.toArray());
    }

    public static class Builder {

        private ItemStack boots;
        private ItemStack pants;
        private ItemStack chestPlate;
        private ItemStack helmet;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder boots(final ItemStack boots) {
            this.boots = boots;
            return this;
        }

        public Builder pants(final ItemStack pants) {
            this.pants = pants;
            return this;
        }

        public Builder chestPlate(final ItemStack chestPlate) {
            this.chestPlate = chestPlate;
            return this;
        }

        public Builder helmet(final ItemStack helmet) {
            this.helmet = helmet;
            return this;
        }

        public Armor build() {
            return new Armor(this.boots, this.pants, this.chestPlate, this.helmet);
        }
    }
}
