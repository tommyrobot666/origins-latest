package io.github.apace100.origins.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.origins.component.item.ItemOriginsComponent;
import io.github.apace100.origins.registry.ModDataComponentTypes;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public abstract class ItemMixin {

    @ModifyReturnValue(method = "use", at = @At("RETURN"))
    private InteractionResult origins$selectItemOrigins(InteractionResult original, final Level level, final Player player, final InteractionHand hand) {

        if (original != InteractionResult.PASS) {
            return original;
        }

        ItemStack stack = player.getItemInHand(hand);
        ItemOriginsComponent itemOrigins = stack.get(ModDataComponentTypes.ORIGIN);

        if (itemOrigins != null && itemOrigins.setOrigin(player)) {

            player.awardStat(Stats.ITEM_USED.get((Item) (Object) this));
            stack.consume(1, player);

            return InteractionResult.CONSUME;//InteractionResultHolder.consume(stack);

        }

        else {
            return original;
        }

    }

}
