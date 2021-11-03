package io.github.fisher2911.minionsplugin.world;

import io.github.fisher2911.fishcore.world.Position;

public record Range(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ) {

    public Region toRegion(final Position origin) {
        final int x = origin.getBlockX();
        final int y = origin.getBlockY();
        final int z = origin.getBlockZ();

        return new RectangularRegion(
                origin.toBukkitLocation().getWorld(),
                origin,
                x - this.minX,
                y - this.minY,
                z - this.minZ,
                x + this.maxX,
                y + this.maxY,
                z + this.maxZ);
    }

    @Override
    public String toString() {
        return "Range{" +
                "minX=" + this.minX +
                ", minY=" + this.minY +
                ", minZ=" + this.minZ +
                ", maxX=" + this.maxX +
                ", maxY=" + this.maxY +
                ", maxZ=" + this.maxZ +
                '}';
    }
}
