package io.github.fisher2911.minionsplugin.util;

import io.github.fisher2911.minionsplugin.keys.Keys;
import io.github.fisher2911.minionsplugin.minion.MinionType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MinionUtil {

    public static long getId(final ArmorStand armorStand) {
        final PersistentDataContainer container = armorStand.getPersistentDataContainer();

        final Long id = container.get(Keys.MINION_KEY, PersistentDataType.LONG);

        return id == null ? -1 : id;
    }

    public static MinionType getMinionType(final ArmorStand armorStand) {
        final PersistentDataContainer container = armorStand.getPersistentDataContainer();

        try {
            final String type = container.get(Keys.MINION_TYPE_KEY, PersistentDataType.STRING);

            if (type == null) {
                return MinionType.UNKNOWN;
            }

            return MinionType.valueOf(type);

        } catch (final IllegalArgumentException exception) {
            return MinionType.UNKNOWN;
        }
    }

}
