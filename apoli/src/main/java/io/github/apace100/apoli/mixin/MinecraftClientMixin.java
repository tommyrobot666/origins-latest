package io.github.apace100.apoli.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.access.OverlaySpriteHolder;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.internal.InternalClient;
import io.github.apace100.apoli.power.type.EntityGlowPowerType;
import io.github.apace100.apoli.power.type.OverlayPowerType;
import io.github.apace100.apoli.power.type.SelfGlowPowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.RunArgs;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.Entity;
//import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin implements OverlaySpriteHolder {

    @Shadow
    public LocalPlayer player;

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Shadow
    public abstract boolean isGameLoadFinished();

    @Shadow
    public abstract TextureManager getTextureManager();

    @ModifyReturnValue(method = "shouldEntityAppearGlowing", at = @At("RETURN"))
    private boolean apoli$makeEntitiesGlow(boolean original, Entity entity) {
        return original
            || (player != entity && PowerHolderComponent.hasPowerType(player, EntityGlowPowerType.class, p -> p.doesApply(entity)))
            || PowerHolderComponent.hasPowerType(entity, SelfGlowPowerType.class, p -> p.doesApply(player));
    }

//    @Unique
//    private OverlayPowerType.SpriteHolder apoli$overlaySpriteHolder;
//
//    @Override
//    public TextureAtlasSprite apoli$getSprite(Identifier id) {
//        return apoli$overlaySpriteHolder.getSprite(id);
//    }
//
//    @Inject(method = "<init>", at = @At(value = "NEW", target = "Gui"))
//    private void apoli$registerCustomAtlases(GameConfig args, CallbackInfo ci) {
//        this.apoli$overlaySpriteHolder = new OverlayPowerType.SpriteHolder(this.getTextureManager());
//        this.resourceManager.registerReloadListener(apoli$overlaySpriteHolder);
//    }
//
//    @Inject(method = "onFinishedLoading", at = @At("HEAD"))
//    private void apoli$postReloadTextures(Minecraft.LoadingContext loadingContext, CallbackInfo ci) {
//        PostLoadTexturesCallback.EVENT.invoker().onPostLoad((Minecraft) (Object) this, this.isFinishedLoading());
//    }
//
//    @Inject(method = "setWorld", at = @At("HEAD"))
//    private void apoli$onJoinWorld(ClientWorld world, CallbackInfo ci) {
//        InternalClient.onClientWorldChanged();
//    }
}
