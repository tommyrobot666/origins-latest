//package io.github.apace100.apoli.mixin;
//
//import net.minecraft.server.network.ServerPlayer;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.util.math.BlockPos;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.gen.Invoker;
//
//import java.util.Optional;
//
//@Mixin(ServerPlayer.class)
//public interface ServerPlayerAccessor {
//
//    @Invoker
//    static Optional<ServerPlayer.RespawnPos> callFindRespawnPosition(ServerWorld world, BlockPos pos, float spawnAngle, boolean spawnForced, boolean alive) {
//        throw new AssertionError();
//    }
//
//}
