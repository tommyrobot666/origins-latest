package io.github.apace100.origins.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.power.type.WaterVisionPowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class WaterVisionMixin extends AbstractClientPlayer {

    private WaterVisionMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyExpressionValue(method = "getWaterVision", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;waterVisionTime:I", ordinal = 0))
    private int origins$ignoreVisibilityDelay(int original) {
        return !PowerHolderComponent.hasPowerType(this, WaterVisionPowerType.class)
            ? original
            : 1000;
    }

}
