package io.github.apace100.apoli.networking;

import io.github.apace100.apoli.networking.packet.VersionHandshakePacket;
import io.github.apace100.apoli.networking.packet.c2s.UseActivePowerTypesC2SPacket;
import io.github.apace100.apoli.networking.packet.s2c.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {

    public static void register() {

        PayloadTypeRegistry.configurationS2C().register(VersionHandshakePacket.PACKET_ID, VersionHandshakePacket.STREAM_CODEC);
        PayloadTypeRegistry.configurationC2S().register(VersionHandshakePacket.PACKET_ID, VersionHandshakePacket.STREAM_CODEC);

        PayloadTypeRegistry.playS2C().register(SyncAttackerS2CPacket.PACKET_ID, SyncAttackerS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(DismountPlayerS2CPacket.PACKET_ID, DismountPlayerS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncStatusEffectS2CPacket.PACKET_ID, SyncStatusEffectS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ShowToastS2CPacket.PACKET_ID, ShowToastS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(MountPlayerS2CPacket.PACKET_ID, MountPlayerS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPowerDataS2CPacket.PACKET_ID, SyncPowerDataS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncBulkPowerDataS2CPacket.PACKET_ID, SyncBulkPowerDataS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPowersS2CPacket.PACKET_ID, SyncPowersS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncEntityTypeTagCacheS2CPacket.PACKET_ID, SyncEntityTypeTagCacheS2CPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncCommandTagsS2CPacket.ID, SyncCommandTagsS2CPacket.CODEC);

        PayloadTypeRegistry.playC2S().register(UseActivePowerTypesC2SPacket.PACKET_ID, UseActivePowerTypesC2SPacket.STREAM_CODEC);

    }

}
