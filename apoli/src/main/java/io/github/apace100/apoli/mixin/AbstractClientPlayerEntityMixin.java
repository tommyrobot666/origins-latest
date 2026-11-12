//package io.github.apace100.apoli.mixin;
//
//import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.mojang.authlib.GameProfile;
//import io.github.apace100.apoli.component.PowerHolderComponent;
//import io.github.apace100.apoli.power.type.ModifyFovPowerType;
//import net.minecraft.client.network.AbstractClientPlayer;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//
//import java.util.List;
//
//@Mixin(AbstractClientPlayer.class)
//public abstract class AbstractClientPlayerMixin extends Player {
//
//    private AbstractClientPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
//        super(world, pos, yaw, gameProfile);
//    }
//
//    @ModifyReturnValue(method = "getFovMultiplier", at = @At(value = "RETURN", ordinal = 0))
//    private float apoli$modifySpyglassFov(float original) {
//        return PowerHolderComponent.modify(this, ModifyFovPowerType.class, original);
//    }
//
//    @WrapOperation(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
//    private float apoli$modifyFov(float delta, float start, float end, Operation<Float> original) {
//
//        List<ModifyFovPowerType> mfps = PowerHolderComponent.getPowerTypes(this, ModifyFovPowerType.class);
//        boolean affectedByFovEffectScale = mfps.isEmpty() || mfps
//            .stream()
//            .anyMatch(ModifyFovPowerType::isAffectedByFovEffectScale);
//
//        float newEnd = PowerHolderComponent.modify(this, ModifyFovPowerType.class, end);
//        float newDelta = affectedByFovEffectScale ? delta : start;
//
//        return original.call(newDelta, start, newEnd);
//
//    }
//
//}
