package io.github.apace100.origins.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.power.type.WaterBreathingPowerType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class WaterBreathingMixin {

    @Mixin(LivingEntity.class)
    public static abstract class BreathingImpl extends Entity {

        private BreathingImpl(EntityType<?> type, Level world) {
            super(type, world);
        }

        @ModifyReturnValue(method = "canBreatheUnderwater", at = @At("RETURN"))
        private boolean origins$breatheUnderwater(boolean original) {
            return original
                || PowerHolderComponent.hasPowerType(this, WaterBreathingPowerType.class);
        }

        @Inject(method = "baseTick", at = @At("TAIL"))
        private void origins$waterBreathingTick(CallbackInfo ci) {
            WaterBreathingPowerType.tick((LivingEntity) (Object) this);
        }

    }

    @Mixin(Player.class)
    public static abstract class TurtleHelmetProxy extends LivingEntity {

        private TurtleHelmetProxy(EntityType<? extends LivingEntity> entityType, Level world) {
            super(entityType, world);
        }

        @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
        private boolean origins$submergedProxy(boolean original) {
            return PowerHolderComponent.hasPowerType(this, WaterBreathingPowerType.class) != original;
        }

    }

}
