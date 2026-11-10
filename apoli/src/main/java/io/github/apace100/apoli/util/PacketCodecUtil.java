//package io.github.apace100.apoli.util;
//
//import io.netty.buffer.ByteBuf;
//import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.codec.ByteBufCodecs;
//
//import java.util.Set;
//
//public final class PacketCodecUtil {
//
//	public static <B extends ByteBuf, E> StreamCodec.ResultFunction<B, E, Set<E>> toHashSet() {
//		return codec -> ByteBufCodecs.collection(ObjectOpenHashSet::new, codec);
//	}
//
//}
