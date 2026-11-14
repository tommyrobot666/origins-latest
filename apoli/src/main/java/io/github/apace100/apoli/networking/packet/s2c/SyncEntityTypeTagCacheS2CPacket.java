package io.github.apace100.apoli.networking.packet.s2c;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.calio.util.BufUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public record SyncEntityTypeTagCacheS2CPacket(Map<Identifier, Collection<Identifier>> subTags) implements CustomPacketPayload {

	public static final Type<SyncEntityTypeTagCacheS2CPacket> PACKET_ID = new Type<>(Apoli.identifier("s2c/sync_entity_type_tag_cache"));
	public static final StreamCodec<FriendlyByteBuf, SyncEntityTypeTagCacheS2CPacket> STREAM_CODEC = StreamCodec.of((b,p) -> p.write(b), SyncEntityTypeTagCacheS2CPacket::read);

	private static SyncEntityTypeTagCacheS2CPacket read(FriendlyByteBuf buf) {
		var ids = BufUtil.readCollection(buf,ArrayList::new,FriendlyByteBuf::readIdentifier);
		var tagDatas = BufUtil.readCollection(buf,ArrayList::new,b -> BufUtil.readCollection(b,ArrayList::new,FriendlyByteBuf::readIdentifier));
		Map<Identifier, Collection<Identifier>> map = HashMap.newHashMap(ids.size());
		for (int i = 0; i < ids.size(); i++) {
			map.put(ids.get(i),tagDatas.get(i));
		}
		return new SyncEntityTypeTagCacheS2CPacket(map);
	}

	private void write(FriendlyByteBuf buf) {
		BufUtil.writeCollection(buf,subTags.keySet(),FriendlyByteBuf::writeIdentifier);
		BufUtil.writeCollection(buf,subTags.values(), (BiConsumer<FriendlyByteBuf, Collection<Identifier>>) (b, c) -> BufUtil.writeCollection(b, c, FriendlyByteBuf::writeIdentifier));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}

}
