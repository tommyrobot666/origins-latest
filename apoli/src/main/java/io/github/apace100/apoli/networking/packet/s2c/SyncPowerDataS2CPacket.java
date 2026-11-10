//package io.github.apace100.apoli.networking.packet.s2c;
//
//import io.github.apace100.apoli.Apoli;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.codec.PacketCodecs;
//import net.minecraft.network.packet.CustomPayload;
//import net.minecraft.resources.Identifier;
//
//public record SyncPowerDataS2CPacket(int entityId, Identifier powerTypeId, NbtCompound powerData) implements CustomPayload {
//
//    public static final Id<SyncPowerDataS2CPacket> PACKET_ID = new Id<>(Apoli.identifier("s2c/sync_power_data"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPowerDataS2CPacket> PACKET_CODEC = StreamCodec.tuple(
//        PacketCodecs.VAR_INT, SyncPowerDataS2CPacket::entityId,
//        Identifier.PACKET_CODEC, SyncPowerDataS2CPacket::powerTypeId,
//        PacketCodecs.NBT_COMPOUND, SyncPowerDataS2CPacket::powerData,
//        SyncPowerDataS2CPacket::new
//    );
//
//    @Override
//    public Id<? extends CustomPayload> getId() {
//        return PACKET_ID;
//    }
//
//}
