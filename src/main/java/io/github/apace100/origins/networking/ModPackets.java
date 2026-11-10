package io.github.apace100.origins.networking;

import io.github.apace100.origins.networking.packet.VersionHandshakePacket;
import io.github.apace100.origins.networking.packet.c2s.ChooseOriginC2SPacket;
import io.github.apace100.origins.networking.packet.c2s.ChooseRandomOriginC2SPacket;
import io.github.apace100.origins.networking.packet.s2c.OriginsInstalledS2CPacket;
import io.github.apace100.origins.networking.packet.s2c.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {

    public static void register() {

        PayloadTypeRegistry.clientboundConfiguration().register(VersionHandshakePacket.PACKET_ID, VersionHandshakePacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundConfiguration().register(OriginsInstalledS2CPacket.PACKET_ID, OriginsInstalledS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundConfiguration().register(VersionHandshakePacket.PACKET_ID, VersionHandshakePacket.STREAM_CODEC);

        PayloadTypeRegistry.clientboundPlay().register(ConfirmOriginS2CPacket.PACKET_ID, ConfirmOriginS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OpenChooseOriginScreenS2CPacket.PACKET_ID, OpenChooseOriginScreenS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncBadgesS2CPacket.PACKET_ID, SyncBadgesS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncOriginLayersS2CPacket.PACKET_ID, SyncOriginLayersS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncOriginsS2CPacket.PACKET_ID, SyncOriginsS2CPacket.STREAM_CODEC);

        PayloadTypeRegistry.serverboundPlay().register(ChooseOriginC2SPacket.PACKET_ID, ChooseOriginC2SPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ChooseRandomOriginC2SPacket.PACKET_ID, ChooseRandomOriginC2SPacket.STREAM_CODEC);

    }

}
