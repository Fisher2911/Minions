package io.github.fisher2911.minionsplugin.minion.food;

import org.bukkit.Material;

import java.util.Map;
import java.util.Set;

public class FoodGroup {

    private final Map<Material, Float> foodValueMap;

    public FoodGroup(final Map<Material, Float> foodValueMap) {
        this.foodValueMap = foodValueMap;
    }

    public float getFoodValue(final Material material) {
        return this.foodValueMap.get(material);
    }

    public Set<Material> getAllFoods() {
        return this.foodValueMap.keySet();
    }

    public Map<Material, Float> getFoodValueMap() {
        return this.foodValueMap;
    }
}
