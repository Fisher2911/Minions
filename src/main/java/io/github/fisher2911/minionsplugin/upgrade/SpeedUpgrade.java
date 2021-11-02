package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.message.MessageHandlerRegistry;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.minionsplugin.MinionsPlugin;
import io.github.fisher2911.minionsplugin.lang.MinionMessages;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpeedUpgrade extends MinionUpgrade<Float> {

    public SpeedUpgrade(final String id,
                        final String displayName,
                        final Map<Integer, Float> levelDataMap,
                        final Map<Integer, Cost> levelCostMap,
                        final ItemStack guiItemStack) {
        super(id, displayName, levelDataMap, levelCostMap, guiItemStack);
    }

    @Override
    public ItemStack getGuiItemStack(final int level, final Float value) {
        final Cost cost = this.getCostAtLevel(level);

        final String moneyPrice;

        if (cost == null) {
            moneyPrice = MessageHandlerRegistry.REGISTRY.get(MinionsPlugin.class).getMessage(MinionMessages.MAX_LEVEL);
        } else {
            moneyPrice = String.valueOf(cost.getMoneyCost());
        }

        final Map<String, String> placeHolders = Map.of(
                Placeholder.LEVEL, String.valueOf(level),
                Placeholder.SPEED,
                String.valueOf(this.getDataAtLevel(level)),
                Placeholder.MONEY_COST, moneyPrice);

        return ItemBuilder.from(this.guiItemStack).
                namePlaceholders(placeHolders).
                lorePlaceholders(placeHolders).build();
    }
}
