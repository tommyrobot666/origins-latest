//package io.github.apace100.apoli.networking.packet;
//
//import io.github.apace100.apoli.Apoli;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//
//public record VersionHandshakePacket(int[] semver) implements CustomPayload {
//
//    public static final Type<VersionHandshakePacket> PACKET_ID = new Type<>(Apoli.identifier("handshake/version"));
//    public static final StreamCodec<PacketByteBuf, VersionHandshakePacket> STREAM_CODEC = StreamCodec.of(VersionHandshakePacket::write, VersionHandshakePacket::read);
//
//    public static VersionHandshakePacket read(PacketByteBuf buf) {
//        return new VersionHandshakePacket(buf.readIntArray());
//    }
//
//    public void write(PacketByteBuf buf) {
//        buf.writeIntArray(semver);
//    }
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return PACKET_ID;
//    }
//
//}
