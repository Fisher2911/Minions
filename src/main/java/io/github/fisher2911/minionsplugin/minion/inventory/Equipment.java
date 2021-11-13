package io.github.fisher2911.minionsplugin.minion.inventory;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Equipment implements IdHolder<String> {

    private final String id;
    private ItemStack mainHand;
    private ItemStack offHand;
    private ItemStack boots;
    private ItemStack pants;
    private ItemStack chestPlate;
    private ItemStack helmet;

    public static Builder builder(final String id) {
        return new Builder(id);
    }

    public Equipment(final String id,
                     final ItemStack mainHand,
                     final ItemStack offHand,
                     final ItemStack boots,
                     final ItemStack pants,
                     final ItemStack chestPlate,
                     final ItemStack helmet) {
        this.id = id;
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.boots = boots;
        this.pants = pants;
        this.chestPlate = chestPlate;
        this.helmet = helmet;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public ItemStack getMainHand() {
        return this.mainHand;
    }

    public void setMainHand(final ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public ItemStack getOffHand() {
        return this.offHand;
    }

    public void setOffHand(final ItemStack offHand) {
        this.offHand = offHand;
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
     * @return converts ItemStacks to array for use with EntityEquipment
     */
    public ItemStack[] toArray() {
        return new ItemStack[]{this.boots, this.pants, this.chestPlate, this.helmet};
    }

    /**
     * Sets the armor an entity is wearing
     *
     * @param entity Entity whose armor is being set
     */

    public void setArmor(final LivingEntity entity) {
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

        private final String id;
        private ItemStack mainHand;
        private ItemStack offHand;
        private ItemStack boots;
        private ItemStack pants;
        private ItemStack chestPlate;
        private ItemStack helmet;

        private Builder(final String id) {
            this.id = id;
        }

        public static Builder create(final String id) {
            return new Builder(id);
        }

        public Builder mainHand(final ItemStack mainHand) {
            this.mainHand = mainHand;
            return this;
        }

        public Builder offHand(final ItemStack offHand) {
            this.offHand = offHand;
            return this;
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

        public Equipment build() {
            return new Equipment(
                    this.id,
                    this.mainHand,
                    this.offHand,
                    this.boots,
                    this.pants,
                    this.chestPlate,
                    this.helmet);
        }
    }
}
