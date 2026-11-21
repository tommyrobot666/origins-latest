package io.github.apace100.apoli.condition.type.item;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ArmorValueItemConditionType extends ItemConditionType {

    public static final TypedDataObjectFactory<ArmorValueItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT),
        data -> new ArmorValueItemConditionType(
            data.get("comparison"),
            data.get("compare_to")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("comparison", conditionType.comparison)
            .set("compare_to", conditionType.compareTo)
    );

    private final Comparison comparison;
    private final int compareTo;

    public ArmorValueItemConditionType(Comparison comparison, int compareTo) {
        this.comparison = comparison;
        this.compareTo = compareTo;
    }

    @Override
    public boolean test(ItemConditionContext context) {
        return isArmor(context.stack().get(DataComponents.EQUIPPABLE))
            && comparison.compare(getProtection(context.stack()), compareTo);
    }

    private double getProtection(ItemStack stack) {
        if (!stack.has(DataComponents.ATTRIBUTE_MODIFIERS)) return 0.0;
        return Objects.requireNonNull(stack.get(DataComponents.ATTRIBUTE_MODIFIERS)).compute(Attributes.ARMOR,0.0,
                Objects.requireNonNull(stack.get(DataComponents.EQUIPPABLE)).slot());
    }

    private boolean isArmor(Equippable equippable) {
        if (equippable == null) return false;
        return equippable.slot().isArmor();
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ItemConditionTypes.ARMOR_VALUE;
    }

}
