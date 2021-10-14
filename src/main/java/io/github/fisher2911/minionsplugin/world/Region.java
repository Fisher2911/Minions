package io.github.fisher2911.minionsplugin.world;

import io.github.fisher2911.fishcore.world.Position;

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

}
