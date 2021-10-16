package io.github.fisher2911.minionsplugin.world;

import io.github.fisher2911.fishcore.world.Position;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RectangularRegion implements Region {

    private final Position origin;
    private final Position min;
    private final Position max;

    public RectangularRegion(final Position origin, final Position min, final Position max) {
        this.origin = origin;
        this.min = min;
        this.max = max;
    }

    public RectangularRegion(final World world, final Position origin,
                             final double minX, final double minY, final double minZ,
                             final double maxX, final double maxY, final double maxZ) {
        this(origin, new Position(world, minX, minY, minZ), new Position(world, maxX, maxY, maxZ));
    }

    @Override
    public boolean contains(final Position position) {
        return this.contains(position,
                this.getMinX(), this.getMinY(), this.getMinZ(),
                this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    @Override
    public boolean onBorder(final Position position) {
        return false;
    }

    @Override
    public boolean within(final Position position) {
        return this.contains(position,
                this.getMinX() + 1, this.getMinY() + 1, this.getMinZ() + 1,
                this.getMaxX() - 1, this.getMaxY() - 1, this.getMaxZ() - 1);
    }

    private boolean contains(final Position position,
                             final double minX,
                             final double minY,
                             final double minZ,
                             final double maxX,
                             final double maxY,
                             final double maxZ) {
        final double x = position.getX();
        final double y = position.getY();
        final double z = position.getZ();

        return minX <= x && x <= maxX &&
        minY <= y && y <= maxY &&
        minZ <= z && z <= maxZ;

    }

    @Override
    public Position getOrigin() {
        return this.origin;
    }

    public double getMinX() {
        return this.min.getX();
    }

    public double getMinY() {
        return this.min.getY();
    }

    public double getMinZ() {
        return this.min.getZ();
    }

    public double getMaxX() {
        return this.max.getX();
    }

    public double getMaxY() {
        return this.max.getY();
    }

    public double getMaxZ() {
        return this.max.getZ();
    }

    @Override
    public Set<Position> getAllPositionsInY(final int minY, final int maxY) {
        final Set<Position> positions = new HashSet<>();

        final Optional<World> worldOptional = this.origin.getWorld();

        if (worldOptional.isEmpty()) {
            return positions;
        }

        final World world = worldOptional.get();

        for (int y = minY; y <= maxY; y++) {
            for (int x = (int) (this.getMinX()); x <= (int) this.getMaxX(); x++) {
                for (int z = (int) (this.getMinZ()); z <= (int) this.getMaxZ(); z++) {

                    positions.add(new Position(world, x, y, z));
                }
            }
        }

        return positions;
    }
}
