package io.github.apace100.calio.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.core.HolderSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(HolderSet.ListBacked.class)
public interface HolderSetBackedAccessor<E> {

	@Invoker
	List<RegistryEntry<E>> callGetEntries();

}
