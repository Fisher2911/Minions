package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.fishcore.economy.Cost;
import io.github.fisher2911.fishcore.message.MessageHandler;
import io.github.fisher2911.fishcore.util.builder.ItemBuilder;
import io.github.fisher2911.minionsplugin.lang.MinionMessages;
import io.github.fisher2911.minionsplugin.lang.Placeholder;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SpeedUpgrade extends MinionUpgrade<Float> {

    public SpeedUpgrade(final @NotNull String id,
                        final @NotNull String displayName,
                        final @NotNull Map<Integer, Float> levelDataMap,
                        final @NotNull Map<Integer, Cost> levelCostMap,
                        final @NotNull ItemStack guiItemStack) {
        super(id, displayName, levelDataMap, levelCostMap, guiItemStack);
    }

    @Override
    public ItemStack getGuiItemStack(final @NotNull BaseMinion<?> minion) {
        final UpgradeData<SpeedUpgrade> speedUpgrade = minion.getUpgrades().getSpeedUpgrade();
        final int level = speedUpgrade.getLevel();

        final Cost cost = this.getCostAtLevel(level);

        final String moneyPrice;

        if (cost == null) {
            moneyPrice = MessageHandler.getInstance().getMessage(MinionMessages.MAX_LEVEL);
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
