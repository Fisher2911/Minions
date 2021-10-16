package io.github.fisher2911.minionsplugin.world;

import io.github.fisher2911.fishcore.world.Position;

import java.util.Set;

public interface Region {

    /**
     *
     * @return if the position is inside the region, including borders
     */
    boolean contains(final Position position);

    boolean onBorder(final Position position);


    /**
     *
     * @return if the position is inside the region, excluding borders
     */

    boolean within(final Position position);

    /**
     *
     * @return origin of the Region, not necessarily the center
     */

    Position getOrigin();

    double getMinY();

    double getMaxY();

    /**
     *
     * @param minY start y level
     * @param maxY end y level
     * @return
     */
    Set<Position> getAllPositionsInY(final int minY, final int maxY);
}
