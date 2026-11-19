package io.github.apace100.apoli.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.access.BlockStateCollisionShapeAccess;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.PhasingPowerType;
import io.github.apace100.apoli.power.type.PreventBlockSelectionPowerType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin extends StateHolder<Block, BlockState> implements BlockStateCollisionShapeAccess {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract VoxelShape getCollisionShape(BlockGetter world, BlockPos pos, CollisionContext context);

    @Unique
    private boolean apoli$queryOriginal = false;

    protected AbstractBlockStateMixin(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, propertyValues);
    }

    @ModifyReturnValue(method = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"))
    private VoxelShape apoli$preventBlockSelection(VoxelShape original, BlockGetter blockView, BlockPos blockPos, CollisionContext context) {

        if (context == CollisionContext.empty()) {
            return original;
        }

        else {
            return PreventBlockSelectionPowerType.doesPrevent(context, blockPos)
                ? Shapes.empty()
                : original;
        }

    }

    @ModifyReturnValue(method = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"))
    private VoxelShape apoli$phaseThroughBlocks(VoxelShape original, @Local BlockPos pos, @Local CollisionContext context) {

        if (context == CollisionContext.empty()) {
            return original;
        }

        else {
            return !apoli$queryOriginal && PhasingPowerType.shouldPhase(context, original, pos)
                ? Shapes.empty()
                : original;
        }

    }

    @WrapWithCondition(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/InsideBlockEffectApplier;Z)V"))
    private boolean apoli$preventOnEntityCollisionCallWhenPhasing(Block instance, BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean b) {
        return !PowerHolderComponent.hasPowerType(entity, PhasingPowerType.class, p -> p.doesApply(blockPos));
    }

    @Override
    public VoxelShape apoli$getOriginalCollisionShape(BlockGetter world, BlockPos pos, CollisionContext context) {

        this.apoli$queryOriginal = true;
        VoxelShape originalShape = this.getCollisionShape(world, pos, context);

        this.apoli$queryOriginal = false;
        return originalShape;

    }

}
