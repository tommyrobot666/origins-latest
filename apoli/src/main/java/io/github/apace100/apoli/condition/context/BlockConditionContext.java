//package io.github.apace100.apoli.condition.context;
//
//import io.github.apace100.apoli.util.SavedBlockPosition;
//import io.github.apace100.apoli.util.context.ConditionContext;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.Level;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Optional;
//
//public record BlockConditionContext(SavedBlockPosition savedBlockPosition) implements ConditionContext {
//
//	public BlockConditionContext(Level world, BlockPos pos, BlockState blockState, Optional<BlockEntity> blockEntity) {
//		this(world, pos, blockState, blockEntity.orElse(null));
//	}
//
//	public BlockConditionContext(Level world, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity) {
//		this(new SavedBlockPosition(world, pos, blockState, blockEntity));
//	}
//
//	public BlockConditionContext(Level world, BlockPos pos) {
//		this(world, pos, world.getBlockState(pos), world.getBlockEntity(pos));
//	}
//
//	public Level world() {
//		return (Level) savedBlockPosition().level();
//	}
//
//	public BlockPos pos() {
//		return savedBlockPosition().getBlockPos();
//	}
//
//	public BlockState blockState() {
//		return savedBlockPosition().getBlockState();
//	}
//
//	public Optional<BlockEntity> blockEntity() {
//		return Optional.ofNullable(savedBlockPosition().getBlockEntity());
//	}
//
//}
