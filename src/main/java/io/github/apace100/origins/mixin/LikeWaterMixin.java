package io.github.apace100.origins.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.origins.power.type.LikeWaterPowerType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LikeWaterMixin extends Entity {

    private LikeWaterMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "getFluidFallingAdjustedMovement", at = @At(value = "RETURN", ordinal = 0))
    private Vec3 origins$modifyVerticalFluidMovingSpeed(Vec3 original) {
        return LikeWaterPowerType.modify(this, original);
    }

}
