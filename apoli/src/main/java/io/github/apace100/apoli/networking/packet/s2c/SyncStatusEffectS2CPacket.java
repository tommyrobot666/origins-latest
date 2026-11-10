//package io.github.apace100.apoli.networking.packet.s2c;
//
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.util.SyncStatusEffectsUtil;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//
//public record SyncStatusEffectS2CPacket(int targetId, CompoundTag statusEffectData, SyncStatusEffectsUtil.UpdateType updateType) implements CustomPayload {
//
//    public static final Type<SyncStatusEffectS2CPacket> PACKET_ID = new Type<>(Apoli.identifier("s2c/sync_status_effect"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStatusEffectS2CPacket> STREAM_CODEC = StreamCodec.composite(
//        ByteBufCodecs.VAR_INT, SyncStatusEffectS2CPacket::targetId,
//        ByteBufCodecs.UNLIMITED_NBT_COMPOUND, SyncStatusEffectS2CPacket::statusEffectData,
//        SyncStatusEffectsUtil.UpdateType.STREAM_CODEC, SyncStatusEffectS2CPacket::updateType,
//        SyncStatusEffectS2CPacket::new
//    );
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return PACKET_ID;
//    }
//
//}
