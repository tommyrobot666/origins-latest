//package io.github.apace100.apoli.networking.packet.s2c;
//
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.util.PacketCodecUtil;
//import io.netty.buffer.ByteBuf;
//import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//
//import java.util.Set;
//
//public record SyncCommandTagsS2CPacket(Set<String> tags, int entityId) implements CustomPacketPayload {
//
//	public static final Type<SyncCommandTagsS2CPacket> ID = new Type<>(Apoli.identifier("s2c/sync_command_tags"));
//	public static final StreamCodec<ByteBuf, SyncCommandTagsS2CPacket> CODEC = StreamCodec.composite(
//		ByteBufCodecs.STRING.collect(PacketCodecUtil.toHashSet()), SyncCommandTagsS2CPacket::tags,
//		ByteBufCodecs.INTEGER, SyncCommandTagsS2CPacket::entityId,
//		SyncCommandTagsS2CPacket::new
//	);
//
//	public SyncCommandTagsS2CPacket(Entity entity) {
//		this(new ObjectOpenHashSet<>(entity.getCommandTags()), entity.getId());
//	}
//
//	@Override
//	public Type<? extends CustomPacketPayload> type() {
//		return ID;
//	}
//
//}
