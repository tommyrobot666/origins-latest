package io.github.apace100.apoli.mixin;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TickableTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;
import java.util.Set;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {

    @Accessor
    ResourceManager getResourceManager();

    @Accessor
    Map<Identifier, AbstractTexture> getByPath();

    @Accessor
    Set<TickableTexture> getTickableTextures();

    @Invoker
    void callSafeClose(Identifier id, AbstractTexture texture);

}
