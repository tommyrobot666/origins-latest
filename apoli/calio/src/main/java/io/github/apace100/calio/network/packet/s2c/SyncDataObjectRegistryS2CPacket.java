package io.github.apace100.calio.network.packet.s2c;

import io.github.apace100.calio.Calio;
import io.github.apace100.calio.registry.DataObjectRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncDataObjectRegistryS2CPacket(DataObjectRegistry<?> registry) implements CustomPacketPayload {

    public static final Type<SyncDataObjectRegistryS2CPacket> PACKET_ID = new Type<>(Calio.identifier("s2c/sync_data_object_registry"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDataObjectRegistryS2CPacket> STREAM_CODEC = StreamCodec.of(SyncDataObjectRegistryS2CPacket::write, SyncDataObjectRegistryS2CPacket::read);

    public static SyncDataObjectRegistryS2CPacket read(RegistryFriendlyByteBuf buf) {

        Identifier registryId = buf.readIdentifier();
        DataObjectRegistry<?> registry = DataObjectRegistry.getRegistry(registryId);

        return new SyncDataObjectRegistryS2CPacket(registry.receive(buf));

    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeIdentifier(registry.getRegistryId());
        registry.send(buf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
