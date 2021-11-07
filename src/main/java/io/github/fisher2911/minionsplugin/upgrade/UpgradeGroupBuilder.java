package io.github.fisher2911.minionsplugin.upgrade;

import io.github.fisher2911.minionsplugin.upgrade.type.FloatUpgrade;
import io.github.fisher2911.minionsplugin.upgrade.type.RangeUpgrade;

public class UpgradeGroupBuilder {

    private String id;
    private FloatUpgrade speedUpgrade;
    private RangeUpgrade rangeUpgrade;
    private FloatUpgrade maxFoodUpgrade;
    private FloatUpgrade foodPerActionUpgrade;

    UpgradeGroupBuilder() {
    }

    public SpeedSetter id(final String id) {
        this.id = id;
        return new SpeedSetter();
    }

    public class SpeedSetter {

        private SpeedSetter() {
        }

        public RangeSetter setSpeedUpgrade(final FloatUpgrade speedUpgrade) {
            UpgradeGroupBuilder.this.speedUpgrade = speedUpgrade;
            return new RangeSetter();
        }
    }

    public class RangeSetter {

        private RangeSetter() {
        }

        public MaxFoodSetter setRangeUpgrade(final RangeUpgrade upgrade) {
            UpgradeGroupBuilder.this.rangeUpgrade = upgrade;
            return new MaxFoodSetter();
        }
    }

    public class MaxFoodSetter {

        private MaxFoodSetter() {
        }

        public FoodPerActionSetter setMaxFoodUpgrade(final FloatUpgrade upgrade) {
            UpgradeGroupBuilder.this.maxFoodUpgrade = upgrade;
            return new FoodPerActionSetter();
        }
    }

    public class FoodPerActionSetter {

        private FoodPerActionSetter() {
        }

        public Builder setFoodPerActionUpgrade(final FloatUpgrade upgrade) {
            UpgradeGroupBuilder.this.foodPerActionUpgrade = upgrade;
            return new Builder();
        }
    }

    public class Builder {

        private Builder() {}

        public UpgradeGroup build() {
            return new UpgradeGroup(
                    UpgradeGroupBuilder.this.id,
                    UpgradeGroupBuilder.this.speedUpgrade,
                    UpgradeGroupBuilder.this.rangeUpgrade,
                    UpgradeGroupBuilder.this.maxFoodUpgrade,
                    UpgradeGroupBuilder.this.foodPerActionUpgrade
            );
        }
    }
}
