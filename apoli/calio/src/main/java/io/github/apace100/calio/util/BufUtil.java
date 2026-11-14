package io.github.apace100.calio.util;

import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    public static <E, C extends Collection<E>> void writeCollection(FriendlyByteBuf buf, C c, BiFunction<FriendlyByteBuf,E,FriendlyByteBuf> w) {
        buf.writeVarInt(c.size());
        for (E thingy : c) {
            w.apply(buf,thingy);
        }
    }

    public static <E, C extends Collection<E>> void writeCollection(FriendlyByteBuf buf, C c, BiConsumer<FriendlyByteBuf,E> w) {
        buf.writeVarInt(c.size());
        for (E thingy : c) {
            w.accept(buf,thingy);
        }
    }
}
