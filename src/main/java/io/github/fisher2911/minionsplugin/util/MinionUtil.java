package io.github.fisher2911.minionsplugin.util;

import io.github.fisher2911.minionsplugin.keys.Keys;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MinionUtil {

    public static long getId(final ArmorStand armorStand) {
        final PersistentDataContainer container = armorStand.getPersistentDataContainer();

        final Long id = container.get(Keys.MINION_KEY, PersistentDataType.LONG);

        return id == null ? -1 : id;
    }

}
