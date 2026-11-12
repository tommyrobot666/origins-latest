//package io.github.apace100.apoli.mixin;
//
//import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.llamalad7.mixinextras.sugar.Local;
//import com.llamalad7.mixinextras.sugar.Share;
//import com.llamalad7.mixinextras.sugar.ref.LocalRef;
//import io.github.apace100.apoli.component.PowerHolderComponent;
//import io.github.apace100.apoli.power.type.*;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.ItemPlacementContext;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.util.InteractionResult;
//import net.minecraft.util.InteractionHand;
//import net.minecraft.util.TypedActionResult;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.List;
//
//@Mixin(BlockItem.class)
//public class BlockItemMixin {
//
//    @ModifyReturnValue(method = "canPlace", at = @At("RETURN"))
//    private boolean apoli$preventBlockPlace(boolean original, ItemPlacementContext context, BlockState state) {
//
//        Player playerEntity = context.getPlayer();
//        if (playerEntity == null) {
//            return original;
//        }
//
//        Direction direction = context.getSide();
//        ItemStack stack = context.getStack();
//        InteractionHand hand = context.getHand();
//
//        BlockPos toPos = context.getBlockPos();
//        BlockPos onPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
//
//        Prioritized.CallInstance<ActiveInteractionPowerType> aipci = new Prioritized.CallInstance<>();
//        int preventBlockPlacePowers = 0;
//
//        aipci.add(playerEntity, PreventBlockPlacePowerType.class, pbpp -> pbpp.doesPrevent(stack, hand, toPos, onPos, direction));
//
//        for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//
//            if (!aipci.hasPowerTypes(i)) {
//                continue;
//            }
//
//            List<PreventBlockPlacePowerType> pbpps = aipci.getPowerTypes(i)
//                .stream()
//                .filter(p -> p instanceof PreventBlockPlacePowerType)
//                .map(p -> (PreventBlockPlacePowerType) p)
//                .toList();
//
//            preventBlockPlacePowers += pbpps.size();
//            pbpps.forEach(pbpp -> pbpp.executeActions(hand, toPos, onPos, direction));
//
//        }
//
//        return preventBlockPlacePowers <= 0 && original;
//
//    }
//
//    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/InteractionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Level;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
//    private void apoli$actionOnBlockPlace(ItemPlacementContext context, CallbackInfoReturnable<InteractionResult> cir, @Local Player user, @Local BlockPos toPos, @Local ItemStack stack, @Share("aipci") LocalRef<Prioritized.CallInstance<ActiveInteractionPowerType>> aipciRef) {
//
//        if (user == null) {
//            return;
//        }
//
//        Direction direction = context.getSide();
//        BlockPos onPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
//        InteractionHand hand = context.getHand();
//
//        Prioritized.CallInstance<ActiveInteractionPowerType> aipci = new Prioritized.CallInstance<>();
//        aipci.add(user, ActionOnBlockPlacePowerType.class, aobpp -> aobpp.shouldExecute(stack, hand, toPos, onPos, direction));
//
//        for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//            aipci.getPowerTypes(i)
//                .stream()
//                .filter(p -> p instanceof ActionOnBlockPlacePowerType)
//                .forEach(p -> ((ActionOnBlockPlacePowerType) p).executeOtherActions(toPos, onPos, direction));
//        }
//
//        aipciRef.set(aipci);
//
//    }
//
//    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/InteractionResult;", at = @At("TAIL"))
//    private void apoli$actionOnBlockPlacePost(ItemPlacementContext context, CallbackInfoReturnable<InteractionResult> cir, @Share("aipci") LocalRef<Prioritized.CallInstance<ActiveInteractionPowerType>> aipciRef) {
//
//        Prioritized.CallInstance<ActiveInteractionPowerType> aipci = aipciRef.get();
//
//        if (aipci != null) {
//
//            for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//                aipci.getPowerTypes(i)
//                    .stream()
//                    .filter(ActionOnBlockPlacePowerType.class::isInstance)
//                    .map(ActionOnBlockPlacePowerType.class::cast)
//                    .forEach(p -> p.executeItemActions(context.getHand()));
//            }
//
//        }
//
//    }
//
//    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;use(Lnet/minecraft/world/Level;Lnet/minecraft/entity/player/Player;Lnet/minecraft/util/InteractionHand;)Lnet/minecraft/util/TypedActionResult;"))
//    private TypedActionResult<ItemStack> apoli$preventItemUseIfFoodBlockItem(BlockItem instance, Level world, Player user, InteractionHand hand, Operation<TypedActionResult<ItemStack>> original) {
//        ItemStack handStack = user.getStackInHand(hand);
//        return PowerHolderComponent.hasPowerType(user, PreventItemUsePowerType.class, p -> p.doesPrevent(handStack))
//            ? TypedActionResult.fail(handStack)
//            : original.call(instance, world, user, hand);
//    }
//
//}
