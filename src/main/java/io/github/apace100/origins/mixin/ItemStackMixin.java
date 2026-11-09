package io.github.apace100.origins.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.origins.registry.ModDataComponentTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public <T extends TooltipProvider> void addToTooltip(final DataComponentType<T> type, final Item.TooltipContext context, final TooltipDisplay display, final Consumer<Component> consumer, final TooltipFlag flag) {
        throw new RuntimeException();
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V", ordinal = 0))
    private void origins$appendItemOriginsTooltip(Item.TooltipContext context, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, /*@Local Consumer<Component> tooltipConsumer,*/ @Local TooltipDisplay display, @Local List<Component> lines) {
        Consumer<Component> tooltipConsumer = lines::add;
        this.addToTooltip(ModDataComponentTypes.ORIGIN, context, display, tooltipConsumer, tooltipFlag);
    }

}
