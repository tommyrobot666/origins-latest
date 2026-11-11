//package io.github.apace100.apoli.networking.packet.s2c;
//
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.util.SyncMobEffectsUtil;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//
//public record SyncMobEffectS2CPacket(int targetId, CompoundTag statusEffectData, SyncMobEffectsUtil.UpdateType updateType) implements CustomPayload {
//
//    public static final Type<SyncMobEffectS2CPacket> PACKET_ID = new Type<>(Apoli.identifier("s2c/sync_status_effect"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMobEffectS2CPacket> STREAM_CODEC = StreamCodec.composite(
//        ByteBufCodecs.VAR_INT, SyncMobEffectS2CPacket::targetId,
//        ByteBufCodecs.UNLIMITED_NBT_COMPOUND, SyncMobEffectS2CPacket::statusEffectData,
//        SyncMobEffectsUtil.UpdateType.STREAM_CODEC, SyncMobEffectS2CPacket::updateType,
//        SyncMobEffectS2CPacket::new
//    );
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return PACKET_ID;
//    }
//
//}
