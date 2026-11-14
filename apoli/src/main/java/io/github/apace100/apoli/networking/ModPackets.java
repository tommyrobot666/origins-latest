package io.github.apace100.apoli.networking;

import io.github.apace100.apoli.networking.packet.VersionHandshakePacket;
import io.github.apace100.apoli.networking.packet.c2s.UseActivePowerTypesC2SPacket;
import io.github.apace100.apoli.networking.packet.s2c.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {

    public static void register() {

        PayloadTypeRegistry.clientboundConfiguration().register(VersionHandshakePacket.PACKET_ID, VersionHandshakePacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundConfiguration().register(VersionHandshakePacket.PACKET_ID, VersionHandshakePacket.STREAM_CODEC);

        PayloadTypeRegistry.clientboundPlay().register(SyncAttackerS2CPacket.PACKET_ID, SyncAttackerS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(DismountPlayerS2CPacket.PACKET_ID, DismountPlayerS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncStatusEffectS2CPacket.PACKET_ID, SyncStatusEffectS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ShowToastS2CPacket.PACKET_ID, ShowToastS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MountPlayerS2CPacket.PACKET_ID, MountPlayerS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncPowerDataS2CPacket.PACKET_ID, SyncPowerDataS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncBulkPowerDataS2CPacket.PACKET_ID, SyncBulkPowerDataS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncPowersS2CPacket.PACKET_ID, SyncPowersS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncEntityTypeTagCacheS2CPacket.PACKET_ID, SyncEntityTypeTagCacheS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SyncCommandTagsS2CPacket.ID, SyncCommandTagsS2CPacket.CODEC);

        PayloadTypeRegistry.serverboundPlay().register(UseActivePowerTypesC2SPacket.PACKET_ID, UseActivePowerTypesC2SPacket.STREAM_CODEC);

    }

}
