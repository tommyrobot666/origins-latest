//package io.github.apace100.apoli.mixin;
//
//import io.github.apace100.apoli.access.EndRespawningEntity;
//import io.github.apace100.apoli.power.type.ActionOnItemUsePowerType;
//import io.github.apace100.apoli.util.PriorityPhase;
//import net.minecraft.world.entity.SlotAccess;
//import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
//import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
//import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
//import net.minecraft.server.network.ServerPlayNetworkHandler;
//import net.minecraft.server.level.ServerPlayer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(ServerPlayNetworkHandler.class)
//public class ServerPlayNetworkHandlerMixin {
//
//    @Shadow
//    public ServerPlayer player;
//
//    @Inject(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;respawnPlayer(Lnet/minecraft/server/network/ServerPlayer;ZLnet/minecraft/entity/Entity$RemovalReason;)Lnet/minecraft/server/network/ServerPlayer;", ordinal = 0))
//    private void saveEndRespawnStatus(ClientStatusC2SPacket packet, CallbackInfo ci) {
//        ((EndRespawningEntity)this.player).apoli$setEndRespawning(true);
//    }
//
//    @Inject(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ChangedDimensionCriterion;trigger(Lnet/minecraft/server/network/ServerPlayer;Lnet/minecraft/registry/ResourceKey;Lnet/minecraft/registry/ResourceKey;)V"))
//    private void undoEndRespawnStatus(ClientStatusC2SPacket packet, CallbackInfo ci) {
//        ((EndRespawningEntity)this.player).apoli$setEndRespawning(false);
//    }
//
//    @Inject(method = "onUpdateSelectedSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/UpdateSelectedSlotC2SPacket;getSelectedSlot()I", ordinal = 0))
//    private void callActionOnUseStopBySwitching(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
//        if(player.isUsingItem()) {
//            ActionOnItemUsePowerType.executeActions(player, SlotAccess.of(player.getInventory(), this.player.getInventory().selectedSlot), player.getActiveItem(), ActionOnItemUsePowerType.TriggerType.STOP, PriorityPhase.ALL);
//        }
//    }
//
//    @Inject(method = "onPlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayer;clearActiveItem()V"))
//    private void callActionOnUseStopBySwappingHands(PlayerActionC2SPacket packet, CallbackInfo ci) {
//        if(player.isUsingItem()) {
//            ActionOnItemUsePowerType.executeActions(player, SlotAccess.of(player.getInventory(), this.player.getInventory().selectedSlot), player.getActiveItem(), ActionOnItemUsePowerType.TriggerType.STOP, PriorityPhase.ALL);
//        }
//    }
//}
