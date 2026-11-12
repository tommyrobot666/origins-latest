//package io.github.apace100.apoli.mixin;
//
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.entity.SlotAccess;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.gen.Accessor;
//import org.spongepowered.asm.mixin.gen.Invoker;
//
//@Mixin(LivingEntity.class)
//public interface LivingEntityAccessor {
//
//    @Accessor("SPRINTING_SPEED_BOOST")
//    static AttributeModifier apoli$getSprintingSpeedBoost() {
//        throw new RuntimeException();
//    }
//
//    @Invoker
//    static SlotAccess callGetStackReference(LivingEntity entity, EquipmentSlot slot) {
//        throw new AssertionError();
//    }
//
//}
