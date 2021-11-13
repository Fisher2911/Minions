package io.github.fisher2911.minionsplugin.config.serializer;

import io.github.fisher2911.fishcore.config.serializer.ItemSerializer;
import io.github.fisher2911.fishcore.configurate.ConfigurationNode;
import io.github.fisher2911.fishcore.configurate.serialize.SerializationException;
import io.github.fisher2911.fishcore.configurate.serialize.TypeSerializer;
import io.github.fisher2911.fishcore.util.helper.Utils;
import io.github.fisher2911.minionsplugin.minion.inventory.Equipment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;

public class EquipmentSerializer implements TypeSerializer<Equipment> {

    private static final String ID = "id";
    private static final String MAIN_HAND = "main-hand";
    private static final String OFF_HAND = "off-hand";
    private static final String BOOTS = "boots";
    private static final String PANTS = "pants";
    private static final String CHESTPLATE = "chestplate";
    private static final String HELMET = "helmet";

    private EquipmentSerializer() {
    }

    public static final EquipmentSerializer INSTANCE = new EquipmentSerializer();

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }


    @Override
    public Equipment deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        final var idNode = this.nonVirtualNode(source, ID);
        final var mainHandNode = source.node(MAIN_HAND);
        final var offHandNode = source.node(OFF_HAND);
        final var bootsNode = source.node(BOOTS);
        final var pantsNode = source.node(PANTS);
        final var chestplateNode = source.node(CHESTPLATE);
        final var helmetNode = source.node(HELMET);

        final String id = Utils.replaceIfNull(idNode.getString(), "");

        final ItemStack mainHand = Utils.replaceIfNull(
                ItemSerializer.INSTANCE.deserialize(ItemStack.class, mainHandNode),
                new ItemStack(Material.AIR)
        );
        final ItemStack offHand = Utils.replaceIfNull(
                ItemSerializer.INSTANCE.deserialize(ItemStack.class, offHandNode),
                new ItemStack(Material.AIR)
        );
        final ItemStack boots = Utils.replaceIfNull(
                ItemSerializer.INSTANCE.deserialize(ItemStack.class, bootsNode),
                new ItemStack(Material.AIR)
        );
        final ItemStack pants = Utils.replaceIfNull(
                ItemSerializer.INSTANCE.deserialize(ItemStack.class, pantsNode),
                new ItemStack(Material.AIR)
        );
        final ItemStack chestplate = Utils.replaceIfNull(
                ItemSerializer.INSTANCE.deserialize(ItemStack.class, chestplateNode),
                new ItemStack(Material.AIR)
        );
        final ItemStack helmet = Utils.replaceIfNull(
                ItemSerializer.INSTANCE.deserialize(ItemStack.class, helmetNode),
                new ItemStack(Material.AIR)
        );

        return Equipment.builder(id).
                mainHand(mainHand).
                offHand(offHand).
                boots(boots).
                pants(pants).
                chestPlate(chestplate).
                helmet(helmet).
                build();
    }

    @Override
    public void serialize(final Type type, @Nullable final Equipment obj, final ConfigurationNode node) throws SerializationException {

    }
}
