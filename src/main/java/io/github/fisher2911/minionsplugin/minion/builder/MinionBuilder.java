package io.github.fisher2911.minionsplugin.minion.builder;

import io.github.fisher2911.fishcore.world.Position;
import io.github.fisher2911.minionsplugin.minion.data.MinionData;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.minion.types.FarmerMinion;
import io.github.fisher2911.minionsplugin.minion.types.MinerMinion;
import io.github.fisher2911.minionsplugin.minion.types.SlayerMinion;
import io.github.fisher2911.minionsplugin.minion.types.WoodcutterMinion;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MinionBuilder {

    private final JavaPlugin plugin;
    private MinionData minionData;
    private Position position;

    private MinionBuilder(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static MinionBuilder builder(final JavaPlugin plugin) {
        return new MinionBuilder(plugin);
    }

    public MinionBuilder minionData(final MinionData minionData) {
        this.minionData = minionData;
        return this;
    }

    public MinionBuilder position(final Position position) {
        this.position = position;
        return this;
    }

    public BaseMinion<?> build() {
        Objects.requireNonNull(this.minionData, "Minion data is required!");
        Objects.requireNonNull(this.position, "Minion position is required!");

        return switch (this.minionData.getMinionClass()) {
            case MINER -> new MinerMinion(
                    this.plugin,
                  this.position,
                  this.minionData
            );

            case SLAYER -> new SlayerMinion(
                    this.plugin,
                    this.position,
                    this.minionData
            );

            case WOODCUTTER -> new WoodcutterMinion(
                    this.plugin,
                    this.position,
                    this.minionData
            );

            case FARMER -> new FarmerMinion(
                    this.plugin,
                    this.position,
                    this.minionData,
                    // todo - change to using upgrades
                    Material.WHEAT
            );
        };
    }
}
