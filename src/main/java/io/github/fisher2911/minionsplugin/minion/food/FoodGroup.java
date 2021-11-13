package io.github.fisher2911.minionsplugin.minion.food;

import io.github.fisher2911.fishcore.util.helper.IdHolder;
import org.bukkit.Material;

import java.util.Map;
import java.util.Set;

public class FoodGroup implements IdHolder<String> {

    private final String id;
    private final Map<Material, Float> foodValueMap;

    public FoodGroup(final String id, final Map<Material, Float> foodValueMap) {
        this.id = id;
        this.foodValueMap = foodValueMap;
    }

    public float getFoodValue(final Material material) {
        return this.foodValueMap.getOrDefault(material, 0f);
    }

    public Set<Material> getAllFoods() {
        return this.foodValueMap.keySet();
    }

    public Map<Material, Float> getFoodValueMap() {
        return this.foodValueMap;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
