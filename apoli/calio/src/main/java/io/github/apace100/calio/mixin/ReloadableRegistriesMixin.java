package io.github.apace100.calio.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.calio.Calio;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ReloadableServerRegistries.class, priority = 900)
public abstract class ReloadableRegistriesMixin {

    @Unique
    private static ResourceManager theManagerThatIWannaAccess;

    @Inject(method = "reload", at = @At(value = "HEAD"))
    private static void calio$cacheNamespaces(LayeredRegistryAccess<RegistryLayer> context, List<Registry.PendingTags<?>> updatedContextTags, ResourceManager manager, Executor executor, CallbackInfoReturnable<CompletableFuture<ReloadableServerRegistries.LoadResult>> cir) {
        theManagerThatIWannaAccess = manager;
    }


    @Inject(method = "lambda$reload$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ReloadableServerRegistries;createAndValidateFullContext(Lnet/minecraft/core/LayeredRegistryAccess;Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/core/RegistryAccess$Frozen;)Lnet/minecraft/server/ReloadableServerRegistries$LoadResult;", shift = At.Shift.AFTER))
    private static void calio$cacheNamespaces(LayeredRegistryAccess context, HolderLookup.Provider loadingContextWithTags, RegistryAccess.Frozen newlyLoadedRegistries, CallbackInfoReturnable<ReloadableServerRegistries.LoadResult> cir) {
        Calio.LOADED_NAMESPACES.put(Unit.INSTANCE, theManagerThatIWannaAccess.getNamespaces());
    }

}
