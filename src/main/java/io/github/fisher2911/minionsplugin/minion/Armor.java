package io.github.fisher2911.minionsplugin.minion;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Armor {

    private ItemStack mainHand;
    private ItemStack offHand;
    private ItemStack boots;
    private ItemStack pants;
    private ItemStack chestPlate;
    private ItemStack helmet;

    public static Builder builder() {
        return new Builder();
    }

    public Armor(final @NotNull ItemStack mainHand,
                 final @NotNull ItemStack offHand,
                 final @NotNull ItemStack boots,
                 final @NotNull ItemStack pants,
                 final @NotNull ItemStack chestPlate,
                 final @NotNull ItemStack helmet) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.boots = boots;
        this.pants = pants;
        this.chestPlate = chestPlate;
        this.helmet = helmet;
    }

    public @NotNull ItemStack getMainHand() {
        return this.mainHand;
    }

    public void setMainHand(final @NotNull ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public @NotNull ItemStack getOffHand() {
        return this.offHand;
    }

    public void setOffHand(final @NotNull ItemStack offHand) {
        this.offHand = offHand;
    }

    public @NotNull ItemStack getBoots() {
        return this.boots;
    }

    public void setBoots(final @NotNull ItemStack boots) {
        this.boots = boots;
    }

    public @NotNull ItemStack getPants() {
        return this.pants;
    }

    public void setPants(final @NotNull ItemStack pants) {
        this.pants = pants;
    }

    public @NotNull ItemStack getChestPlate() {
        return this.chestPlate;
    }

    public void setChestPlate(final @NotNull ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    public @NotNull ItemStack getHelmet() {
        return this.helmet;
    }

    public void setHelmet(final @NotNull ItemStack helmet) {
        this.helmet = helmet;
    }

    /**
     *
     * @return converts ItemStacks to array for use with EntityEquipment
     */

    public @NotNull ItemStack[] toArray() {
        return new ItemStack[]{this.boots, this.pants, this.chestPlate, this.helmet};
    }

    /**
     *
     * @param entity Entity whose armor is being set
     */


    public void setArmor(final @NotNull LivingEntity entity) {
        final EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) {
            return;
        }
        equipment.setArmorContents(this.toArray());
        equipment.setItemInMainHand(this.mainHand);
        equipment.setItemInOffHand(this.offHand);
        equipment.setBoots(this.boots);
        equipment.setLeggings(this.pants);
        equipment.setChestplate(this.chestPlate);
        equipment.setHelmet(this.helmet);

    }

    public static class Builder {

        private ItemStack mainHand;
        private ItemStack offHand;
        private ItemStack boots;
        private ItemStack pants;
        private ItemStack chestPlate;
        private ItemStack helmet;

        private Builder() {
        }

        public static @NotNull Builder create() {
            return new Builder();
        }

        public @NotNull Builder mainHand(final @NotNull ItemStack mainHand) {
            this.mainHand = mainHand;
            return this;
        }

        public @NotNull Builder offHand(final @NotNull ItemStack offHand) {
            this.offHand = offHand;
            return this;
        }

        public @NotNull Builder boots(final @NotNull ItemStack boots) {
            this.boots = boots;
            return this;
        }

        public @NotNull Builder pants(final @NotNull ItemStack pants) {
            this.pants = pants;
            return this;
        }

        public @NotNull Builder chestPlate(final @NotNull ItemStack chestPlate) {
            this.chestPlate = chestPlate;
            return this;
        }

        public @NotNull Builder helmet(final @NotNull ItemStack helmet) {
            this.helmet = helmet;
            return this;
        }

        public @NotNull Armor build() {
            return new Armor(
                    this.mainHand,
                    this.offHand,
                    this.boots,
                    this.pants,
                    this.chestPlate,
                    this.helmet);
        }
    }
}
