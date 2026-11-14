package io.github.apace100.apoli.networking.packet;

import io.github.apace100.apoli.Apoli;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record VersionHandshakePacket(int[] semver) implements CustomPacketPayload {

    public static final Type<VersionHandshakePacket> PACKET_ID = new Type<>(Apoli.identifier("handshake/version"));
    public static final StreamCodec<FriendlyByteBuf, VersionHandshakePacket> STREAM_CODEC = StreamCodec.of(VersionHandshakePacket::write, VersionHandshakePacket::read);

    public static VersionHandshakePacket read(FriendlyByteBuf buf) {
        return new VersionHandshakePacket(buf.readVarIntArray());
    }

    public static void write(FriendlyByteBuf buf, VersionHandshakePacket p) {
        buf.writeVarIntArray(p.semver);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}
