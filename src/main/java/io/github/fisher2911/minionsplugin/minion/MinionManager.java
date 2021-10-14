package io.github.fisher2911.minionsplugin.minion;

import io.github.fisher2911.fishcore.manager.Manager;
import io.github.fisher2911.minionsplugin.minion.types.BaseMinion;
import io.github.fisher2911.minionsplugin.keys.Keys;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MinionManager<V extends BaseMinion> extends Manager<Long, V> {

    public long getId(final ArmorStand armorStand) {
        final PersistentDataContainer container = armorStand.getPersistentDataContainer();

        final Long id = container.get(Keys.MINION_KEY, PersistentDataType.LONG);

        return id == null ? -1 : id;
    }

}
