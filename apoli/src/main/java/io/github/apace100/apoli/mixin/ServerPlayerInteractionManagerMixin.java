//package io.github.apace100.apoli.mixin;
//
//import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.llamalad7.mixinextras.sugar.Share;
//import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
//import com.llamalad7.mixinextras.sugar.ref.LocalRef;
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.component.PowerHolderComponent;
//import io.github.apace100.apoli.power.type.*;
//import io.github.apace100.apoli.util.ActionResultUtil;
//import io.github.apace100.apoli.util.BlockUsagePhase;
//import io.github.apace100.apoli.util.PriorityPhase;
//import io.github.apace100.apoli.util.SavedBlockPosition;
//import net.minecraft.block.BlockState;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.level.ServerPlayerInteractionManager;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.InteractionResult;
//import net.minecraft.util.InteractionHand;
//import net.minecraft.util.ItemActionResult;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.world.Level;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Slice;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.List;
//
//@Mixin(ServerPlayerInteractionManager.class)
//public class ServerPlayerInteractionManagerMixin {
//
//    @Shadow
//    protected ServerLevel world;
//
//    @Shadow
//    @Final
//    protected ServerPlayer player;
//
//    @Unique
//    private Direction apoli$blockBreakDirection;
//
//    @Inject(method = "processBlockBreakingAction", at = @At("HEAD"))
//    private void apoli$cacheBlockBreakDirection(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
//        this.apoli$blockBreakDirection = direction;
//    }
//
//    @ModifyExpressionValue(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerLevel;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
//    private boolean apoli$cacheBlockRemovedResult(boolean original, @Share("blockRemoved") LocalBooleanRef blockRemovedRef) {
//        blockRemovedRef.set(original);
//        return original;
//    }
//
//    @Inject(method = "tryBreakBlock", at = {@At(value = "RETURN", ordinal = 3), @At(value = "RETURN", ordinal = 4)})
//    private void apoli$actionOnBlockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Share(value = "breakingBlock", namespace = Apoli.MODID) LocalRef<SavedBlockPosition> breakingBlockRef, @Share(value = "modifiedHarvest", namespace = Apoli.MODID) LocalBooleanRef modifiedHarvestRef, @Share("blockRemoved") LocalBooleanRef blockRemovedRef) {
//        boolean harvestedSuccessfully = blockRemovedRef.get() && modifiedHarvestRef.get();
//        PowerHolderComponent.withPowerTypes(this.player, ActionOnBlockBreakPowerType.class, powerType -> powerType.doesApply(breakingBlockRef.get(), harvestedSuccessfully), powerType -> powerType.executeActions(pos, apoli$blockBreakDirection));
//    }
//
//    @WrapOperation(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onUse(Lnet/minecraft/world/Level;Lnet/minecraft/entity/player/Player;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/InteractionResult;"))
//    private InteractionResult apoli$beforeUseBlock(BlockState state, Level world, Player player, BlockHitResult hitResult, Operation<InteractionResult> original, ServerPlayer mPlayer, Level mWorld, ItemStack mStack, InteractionHand mHand, @Share("zeroPriority$onBlock") LocalRef<InteractionResult> zeroPriority$onBlockRef, @Share("zeroPriority$itemOnBlock") LocalRef<InteractionResult> zeroPriority$itemOnBlockRef) {
//
//        ItemStack stackInHand = player.getStackInHand(mHand);
//        BlockUsagePhase usePhase = BlockUsagePhase.BLOCK;
//
//        if (PreventBlockUsePowerType.doesPrevent(player, usePhase, hitResult, stackInHand, mHand)) {
//            return InteractionResult.FAIL;
//        }
//
//        Prioritized.CallInstance<ActiveInteractionPowerType> aipci = new Prioritized.CallInstance<>();
//        aipci.add(player, ActionOnBlockUsePowerType.class, p -> p.shouldExecute(usePhase, PriorityPhase.BEFORE, hitResult, mHand, stackInHand));
//
//        for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//
//            if (!aipci.hasPowerTypes(i)) {
//                continue;
//            }
//
//            List<ActiveInteractionPowerType> aips = aipci.getPowerTypes(i);
//            InteractionResult previousResult = InteractionResult.PASS;
//
//            for (ActiveInteractionPowerType aip : aips) {
//
//                InteractionResult currentResult = aip instanceof ActionOnBlockUsePowerType aobup
//                    ? aobup.executeAction(hitResult, mHand)
//                    : InteractionResult.PASS;
//
//                if (ActionResultUtil.shouldOverride(previousResult, currentResult)) {
//                    previousResult = currentResult;
//                }
//
//            }
//
//            if (i == 0) {
//                zeroPriority$onBlockRef.set(previousResult);
//                continue;
//            }
//
//            if (previousResult == InteractionResult.PASS) {
//                continue;
//            }
//
//            if (previousResult.shouldSwingHand()) {
//                player.swingHand(mHand);
//            }
//
//            return previousResult;
//
//        }
//
//        return original.call(state, world, player, hitResult);
//
//    }
//
//    @ModifyReturnValue(method = "interactBlock", at = @At(value = "RETURN", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayer;getMainHandStack()Lnet/minecraft/item/ItemStack;")))
//    private InteractionResult apoli$afterUseBlock(InteractionResult original, ServerPlayer player, Level world, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, @Share("zeroPriority$onBlock") LocalRef<InteractionResult> zeroPriority$onBlockRef) {
//
//        InteractionResult zeroPriority$onBlock = zeroPriority$onBlockRef.get();
//        InteractionResult newResult = InteractionResult.PASS;
//
//        if (zeroPriority$onBlock != null && zeroPriority$onBlock != InteractionResult.PASS) {
//            newResult = zeroPriority$onBlock;
//        }
//
//        else if (original == InteractionResult.PASS) {
//
//            Prioritized.CallInstance<ActiveInteractionPowerType> aipci = new Prioritized.CallInstance<>();
//            aipci.add(player, ActionOnBlockUsePowerType.class, p -> p.shouldExecute(BlockUsagePhase.BLOCK, PriorityPhase.AFTER, hitResult, hand, stack));
//
//            for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//
//                if (!aipci.hasPowerTypes(i)) {
//                    continue;
//                }
//
//                List<ActiveInteractionPowerType> aips = aipci.getPowerTypes(i);
//                InteractionResult previousResult = InteractionResult.PASS;
//
//                for (ActiveInteractionPowerType aip : aips) {
//
//                    InteractionResult currentResult = aip instanceof ActionOnBlockUsePowerType aobup
//                        ? aobup.executeAction(hitResult, hand)
//                        : InteractionResult.PASS;
//
//                    if (ActionResultUtil.shouldOverride(previousResult, currentResult)) {
//                        previousResult = currentResult;
//                    }
//
//                }
//
//                if (previousResult != InteractionResult.PASS) {
//                    newResult = previousResult;
//                    break;
//                }
//
//            }
//
//        }
//
//        if (newResult.shouldSwingHand()) {
//            player.swingHand(hand);
//        }
//
//        return ActionResultUtil.shouldOverride(original, newResult)
//            ? newResult
//            : original;
//
//    }
//
//    @WrapOperation(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onUseWithItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/Level;Lnet/minecraft/entity/player/Player;Lnet/minecraft/util/InteractionHand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ItemActionResult;"))
//    private ItemActionResult apoli$beforeItemUseOnBlock(BlockState state, ItemStack stack, Level world, Player player, InteractionHand hand, BlockHitResult hitResult, Operation<ItemActionResult> original, @Share("zeroPriority$itemOnBlock") LocalRef<InteractionResult> zeroPriority$itemOnBlockRef) {
//
//        BlockUsagePhase usePhase = BlockUsagePhase.ITEM;
//        if (PreventBlockUsePowerType.doesPrevent(player, usePhase, hitResult, stack, hand)) {
//            return ItemActionResult.FAIL;
//        }
//
//        Prioritized.CallInstance<ActiveInteractionPowerType> aipci = new Prioritized.CallInstance<>();
//        aipci.add(player, ActionOnBlockUsePowerType.class, p -> p.shouldExecute(usePhase, PriorityPhase.BEFORE, hitResult, hand, stack));
//
//        for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//
//            if (!aipci.hasPowerTypes(i)) {
//                continue;
//            }
//
//            List<ActiveInteractionPowerType> aips = aipci.getPowerTypes(i);
//            InteractionResult previousResult = InteractionResult.PASS;
//
//            for (ActiveInteractionPowerType aip : aips) {
//
//                InteractionResult currentResult = aip instanceof ActionOnBlockUsePowerType aobup
//                    ? aobup.executeAction(hitResult, hand)
//                    : InteractionResult.PASS;
//
//                if (ActionResultUtil.shouldOverride(previousResult, currentResult)) {
//                    previousResult = currentResult;
//                }
//
//            }
//
//            if (i == 0) {
//                zeroPriority$itemOnBlockRef.set(previousResult);
//                continue;
//            }
//
//            if (previousResult == InteractionResult.PASS) {
//                continue;
//            }
//
//            if (previousResult.shouldSwingHand()) {
//                player.swingHand(hand);
//            }
//
//            return switch (previousResult) {
//                case SUCCESS, SUCCESS_NO_ITEM_USED ->
//                    ItemActionResult.SUCCESS;
//                case CONSUME ->
//                    ItemActionResult.CONSUME;
//                case CONSUME_PARTIAL ->
//                    ItemActionResult.CONSUME_PARTIAL;
//                case FAIL ->
//                    ItemActionResult.FAIL;
//                default ->
//                    throw new IllegalStateException("Unexpected value: " + previousResult);
//            };
//
//        }
//
//        return original.call(state, stack, world, player, hand, hitResult);
//
//    }
//
//    @ModifyReturnValue(method = "interactBlock", at = @At("RETURN"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/util/ItemActionResult;isAccepted()Z")))
//    private InteractionResult apoli$afterItemUseOnBlock(InteractionResult original, ServerPlayer player, Level world, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, @Share("zeroPriority$itemOnBlock") LocalRef<InteractionResult> zeroPriority$itemOnBlockRef) {
//
//        InteractionResult zeroPriority$itemOnBlock = zeroPriority$itemOnBlockRef.get();
//        InteractionResult newResult = InteractionResult.PASS;
//
//        if (zeroPriority$itemOnBlock != null && zeroPriority$itemOnBlock != InteractionResult.PASS) {
//            newResult = zeroPriority$itemOnBlock;
//        }
//
//        else if (original == InteractionResult.PASS) {
//
//            Prioritized.CallInstance<ActiveInteractionPowerType> aipci = new Prioritized.CallInstance<>();
//            aipci.add(player, ActionOnBlockUsePowerType.class, p -> p.shouldExecute(BlockUsagePhase.ITEM, PriorityPhase.AFTER, hitResult, hand, stack));
//
//            for (int i = aipci.getMaxPriority(); i >= aipci.getMinPriority(); i--) {
//
//                if (!aipci.hasPowerTypes(i)) {
//                    continue;
//                }
//
//                List<ActiveInteractionPowerType> aips = aipci.getPowerTypes(i);
//                InteractionResult previousResult = InteractionResult.PASS;
//
//                for (ActiveInteractionPowerType aip : aips) {
//
//                    InteractionResult currentResult = aip instanceof ActionOnBlockUsePowerType aobup
//                        ? aobup.executeAction(hitResult, hand)
//                        : InteractionResult.PASS;
//
//                    if (ActionResultUtil.shouldOverride(previousResult, currentResult)) {
//                        previousResult = currentResult;
//                    }
//
//                }
//
//                if (previousResult != InteractionResult.PASS) {
//                    newResult = previousResult;
//                    break;
//                }
//
//            }
//
//        }
//
//        if (newResult.shouldSwingHand()) {
//            player.swingHand(hand);
//        }
//
//        return ActionResultUtil.shouldOverride(original, newResult)
//            ? newResult
//            : original;
//
//    }
//
//}
