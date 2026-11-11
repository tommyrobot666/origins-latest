package io.github.apace100.calio.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.calio.CalioServer;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.permissions.PermissionSet;
//import net.minecraft.tags.TagLoader; might be usefull
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {

    @Inject(method = "loadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"))
    private static void calio$cacheDynamicRegistries(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> contextLayers, List<Registry.PendingTags<?>> updatedContextTags, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions, Executor backgroundExecutor, Executor mainThreadExecutor, CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir, @Local ReloadableServerResources result) {
        CalioServer.DYNAMIC_REGISTRIES.put(Unit.INSTANCE, contextLayers.compositeAccess());
        CalioServer.DATA_PACK_CONTENTS.put(Unit.INSTANCE, result);
    }

    @Inject(method = "updateComponentsAndStaticRegistryTags", at = @At("RETURN"))
    private void calio$cacheRegistryTags(CallbackInfo ci) {

        CalioServer.REGISTRY_TAGS.clear();

        BuiltInRegistries.REGISTRY.entrySet().forEach(
                e -> e.getValue().getTags().forEach(
                        t -> putInCache(t)));

    }

    private static <T> void putInCache(HolderSet.Named<T> t) {
        CalioServer.REGISTRY_TAGS.put(t.key(), ((Collection<Holder<?>>) (Object) t.stream().toList()));
    }

}
