package io.github.apace100.calio.util;

import com.mojang.serialization.DynamicOps;
import io.github.apace100.calio.CalioServer;
import io.github.apace100.calio.mixin.HolderLookupAdapterAccessor;
import io.github.apace100.calio.mixin.RegistryOpsAccessor;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public class RegistryOpsUtil {

	public static <I> Optional<RegistryOps<I>> getOrCreate(DynamicOps<I> ops) {

		if (ops instanceof RegistryOps<I> regOps) {
			return Optional.of(regOps);
		}

		else {
			return CalioServer.getDynamicRegistries().map(drm -> drm.createSerializationContext(ops));
		}

	}

	public static <I> Optional<HolderLookup.Provider> getWrapperLookup(DynamicOps<I> ops) {
		return CalioServer.getDynamicRegistries()
			.map(HolderLookup.Provider.class::cast)
			.or(() -> {

				RegistryOps.RegistryInfoLookup infoGetter = getOrCreate(ops)
					.map(RegistryOpsAccessor.class::cast)
					.map(RegistryOpsAccessor::getLookupProvider)
					.orElse(null);

				return infoGetter instanceof HolderLookupAdapterAccessor cachedInfoGetter
					? Optional.of(cachedInfoGetter.getLookupProvider())
					: Optional.empty();

			});
	}

	public static <T, I> Optional<HolderGetter<T>> getEntryLookup(DynamicOps<I> ops, ResourceKey<? extends Registry<T>> registryRef) {
		return CalioServer.getDynamicRegistries()
			.flatMap(registries -> registries.lookup(registryRef))
			.map(impl -> (HolderGetter<T>) impl)
			.or(() -> getOrCreate(ops)
				.flatMap(registryOps -> registryOps.getter(registryRef)));
	}

}
