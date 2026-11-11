//package io.github.apace100.apoli.mixin;
//
//import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//import io.github.apace100.apoli.power.type.EdibleItemPowerType;
//import net.minecraft.client.network.AbstractClientPlayer;
//import net.minecraft.client.render.item.HeldItemRenderer;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.util.InteractionHand;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//
//@Mixin(HeldItemRenderer.class)
//public abstract class HeldItemRendererMixin {
//
//    @ModifyExpressionValue(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
//    private boolean apoli$overrideSpecialTransforms(boolean original, AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack stack) {
//        return original && EdibleItemPowerType.get(stack)
//            .map(EdibleItemPowerType::getFoodProperties)
//            .map(fc -> player.canConsume(fc.canAlwaysEat()))
//            .orElse(true);
//    }
//
//}
