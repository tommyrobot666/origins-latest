package io.github.apace100.apoli.mixin;

import com.google.common.collect.AbstractIterator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.access.BlockCollisionSpliteratorAccess;
import io.github.apace100.apoli.access.BlockStateCollisionShapeAccess;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockCollisions.class)
public abstract class BlockCollisionSpliteratorMixin<T> extends AbstractIterator<T> implements BlockCollisionSpliteratorAccess {

    @Unique
    boolean apoli$getOriginalShapes;

    @Override
    public boolean apoli$shouldGetOriginalShapes() {
        return apoli$getOriginalShapes;
    }

    @Override
    public void apoli$setGetOriginalShapes(boolean getOriginalShapes) {
        this.apoli$getOriginalShapes = getOriginalShapes;
    }

    @WrapOperation(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape apoli$overrideCollisionShapeQuery(CollisionContext instance, BlockState state, CollisionGetter collisionGetter, BlockPos pos, Operation<VoxelShape> original) {
        return state instanceof BlockStateCollisionShapeAccess shapeAccess && this.apoli$shouldGetOriginalShapes()
            ? shapeAccess.apoli$getOriginalCollisionShape(collisionGetter, pos, instance)
            : original.call(instance, state, collisionGetter, pos);
    }

}
