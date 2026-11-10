//package io.github.apace100.apoli.networking.packet;
//
//import io.github.apace100.apoli.Apoli;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//
//public record VersionHandshakePacket(int[] semver) implements CustomPayload {
//
//    public static final Type<VersionHandshakePacket> PACKET_ID = new Type<>(Apoli.identifier("handshake/version"));
//    public static final StreamCodec<FriendlyByteBuf, VersionHandshakePacket> STREAM_CODEC = StreamCodec.of(VersionHandshakePacket::write, VersionHandshakePacket::read);
//
//    public static VersionHandshakePacket read(FriendlyByteBuf buf) {
//        return new VersionHandshakePacket(buf.readIntArray());
//    }
//
//    public void write(FriendlyByteBuf buf) {
//        buf.writeIntArray(semver);
//    }
//
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return PACKET_ID;
//    }
//
//}
