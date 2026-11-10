package io.github.apace100.calio.mixin;

import net.minecraft.registry.entry.Holder;
import net.minecraft.core.HolderSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(HolderSet.Named.class)
public interface HolderSetNamedAccessor<T> {

    @Invoker
    List<Holder<T>> callGetEntries();

}
