package io.github.apace100.apoli.util;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributedEntityAttributeModifier(Holder<Attribute> attribute, AttributeModifier modifier) {

}
