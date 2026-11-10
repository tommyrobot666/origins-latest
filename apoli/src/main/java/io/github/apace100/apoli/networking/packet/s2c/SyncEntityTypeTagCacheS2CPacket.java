//package io.github.apace100.apoli.networking.packet.s2c;
//
//import io.github.apace100.apoli.Apoli;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.Identifier;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//public record SyncEntityTypeTagCacheS2CPacket(Map<Identifier, Collection<Identifier>> subTags) implements CustomPayload {
//
//	public static final Type<SyncEntityTypeTagCacheS2CPacket> PACKET_ID = new Type<>(Apoli.identifier("s2c/sync_entity_type_tag_cache"));
//	public static final StreamCodec<PacketByteBuf, SyncEntityTypeTagCacheS2CPacket> STREAM_CODEC = StreamCodec.of(SyncEntityTypeTagCacheS2CPacket::write, SyncEntityTypeTagCacheS2CPacket::read);
//
//	private static SyncEntityTypeTagCacheS2CPacket read(PacketByteBuf buf) {
//		return new SyncEntityTypeTagCacheS2CPacket(buf.readMap(PacketByteBuf::readIdentifier, valBuf -> valBuf.readCollection(ArrayList::new, PacketByteBuf::readIdentifier)));
//	}
//
//	private void write(PacketByteBuf buf) {
//		buf.writeMap(subTags, PacketByteBuf::writeIdentifier, (valBuf, value) -> valBuf.writeCollection(value, PacketByteBuf::writeIdentifier));
//	}
//
//	@Override
//	public Type<? extends CustomPacketPayload> type() {
//		return PACKET_ID;
//	}
//
//}
