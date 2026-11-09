package io.github.apace100.origins.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.power.type.ConduitPowerOnLandPowerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ConduitBlockEntity.class)
public abstract class ConduitPowerOnLandMixin {

    @ModifyExpressionValue(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
    private static boolean origins$applyConduitPower(boolean original, @Local Player player) {
        return original
            || PowerHolderComponent.hasPowerType(player, ConduitPowerOnLandPowerType.class);
    }

}
