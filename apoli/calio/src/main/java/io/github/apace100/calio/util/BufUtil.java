package io.github.apace100.calio.util;

import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

public class BufUtil {
    public static <E, C extends Collection<E>> C readCollection(FriendlyByteBuf buf, IntFunction<C> c, Function<FriendlyByteBuf,E> r){
        int size = buf.readVarInt();
        C out = c.apply(size);
        for (int i = 0; i < size; i++) {
            out.add(r.apply(buf));
        }
        return out;
    }

    public static <E, C extends Collection<E>> void writeCollection(FriendlyByteBuf buf, C c, BiConsumer<FriendlyByteBuf,E> w) {
        buf.writeVarInt(c.size());
        for (E thingy : c) {
            w.accept(buf,thingy);
        }
    }

    public static <K, V> Map<K, V> readMap(FriendlyByteBuf buf, Function<FriendlyByteBuf,K> readKey, Function<FriendlyByteBuf,V> readValue) {
        ArrayList<K> keys = readCollection(buf, ArrayList::new, readKey);
        ArrayList<V> values = readCollection(buf, ArrayList::new, readValue);
        Map<K, V> map = HashMap.newHashMap(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    public static <K, V> void writeMap(FriendlyByteBuf buf, Collection<K> keys, BiConsumer<FriendlyByteBuf,K> writeKey, Collection<V> values, BiConsumer<FriendlyByteBuf,V> writeValue) {
        writeCollection(buf,keys,writeKey);
        writeCollection(buf,values,writeValue);
    }

    public static <K, V> void writeMap(FriendlyByteBuf buf, Map<K, V> map, BiConsumer<FriendlyByteBuf,K> writeKey, BiConsumer<FriendlyByteBuf,V> writeValue) {
        writeMap(buf,map.keySet(),writeKey,map.values(),writeValue);
    }
}
