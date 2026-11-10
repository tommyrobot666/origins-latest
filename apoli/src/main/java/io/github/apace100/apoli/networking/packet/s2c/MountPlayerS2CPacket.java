//package io.github.apace100.apoli.networking.packet.s2c;
//
//import io.github.apace100.apoli.Apoli;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.codec.PacketCodecs;
//import net.minecraft.network.packet.CustomPayload;
//
//public record MountPlayerS2CPacket(int actorId, int targetId) implements CustomPayload {
//
//    public static final Id<MountPlayerS2CPacket> PACKET_ID = new Id<>(Apoli.identifier("s2c/mount_player"));
//    public static final StreamCodec<PacketByteBuf, MountPlayerS2CPacket> PACKET_CODEC = StreamCodec.tuple(
//        PacketCodecs.VAR_INT, MountPlayerS2CPacket::actorId,
//        PacketCodecs.VAR_INT, MountPlayerS2CPacket::targetId,
//        MountPlayerS2CPacket::new
//    );
//
//    @Override
//    public Id<? extends CustomPayload> getId() {
//        return PACKET_ID;
//    }
//
//}
