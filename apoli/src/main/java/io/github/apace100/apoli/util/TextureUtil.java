package io.github.apace100.apoli.util;

import com.mojang.serialization.DataResult;
import io.github.apace100.apoli.mixin.TextureManagerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.function.Function;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TextureUtil {

    public static final Identifier GUI_ATLAS_TEXTURE = Identifier.withDefaultNamespace("textures/atlas/gui.png");

    /**
     *  <p>Tries loading the texture that corresponds with the specified {@link Identifier}.</p>
     *
     *  @param id   the {@link Identifier} of the texture to load
     *
     *  @return     the {@link Identifier} of the texture wrapped in a {@link DataResult}
     */
    public static DataResult<Identifier> tryLoadingTexture(Identifier id) {
        return tryLoadingTexture(id, _id -> "Texture \"" + id + "\" does not exist!", _id -> "Failed to load texture \"" + id + "\"\n");
    }

    /**
     *  <p>Tries loading the texture that corresponds with the specified {@link Identifier}.</p>
     *
     *  @param id               the {@link Identifier} of the texture to load
     *  @param missingErr       the error message to use if the texture doesn't exist
     *  @param loadFailureErr   the error message to use if the texture failed to load
     *
     *  @return                 the {@link Identifier} of the texture wrapped in a {@link DataResult}
     */
    public static DataResult<Identifier> tryLoadingTexture(Identifier id, Function<Identifier, String> missingErr, Function<Identifier, String> loadFailureErr) {

        TextureManagerAccessor textureManagerAccessor = (TextureManagerAccessor) Minecraft.getInstance().getTextureManager();

        AbstractTexture texture = textureManagerAccessor.getByPath().get(id);
        StringBuilder errorMessage = new StringBuilder();

        boolean erred = false;

        if (texture != null) {
            return texture == textureManagerAccessor.getByPath().get(MissingTextureAtlasSprite.getLocation())
                ? DataResult.error(() -> missingErr.apply(id))
                : DataResult.success(id);
        }

        try {
            texture = new SimpleTexture(id);
            ((SimpleTexture) texture).loadContents(textureManagerAccessor.getResourceManager());
        } catch (IOException io) {

            texture = new SimpleTexture(MissingTextureAtlasSprite.getLocation());

            if (id != MissingTextureAtlasSprite.getLocation()) {

                errorMessage
                    .append(loadFailureErr.apply(id))
                    .append(ExceptionUtils.getStackTrace(io));

                erred = true;

            }

        }

        AbstractTexture prevTexture = textureManagerAccessor.getByPath().put(id, texture);
        if (prevTexture != null && prevTexture != textureManagerAccessor.getByPath().get(MissingTextureAtlasSprite.getLocation())) {
            textureManagerAccessor.callSafeClose(id, prevTexture);
        }

        return erred
            ? DataResult.error(errorMessage::toString)
            : DataResult.success(id);

    }

    /**
     *  <p>Tries loading the sprite that corresponds with the first {@link Identifier} from the
     *  texture atlas that corresponds with the second {@link Identifier}.</p>
     *
     *  @param spriteId     the {@link Identifier} of the sprite to load
     *  @param atlasId      the {@link Identifier} of the texture atlas
     *
     *  @return             the {@link Identifier} of the sprite wrapped in a {@link DataResult}
     */
    public static DataResult<Identifier> tryLoadingSprite(Identifier spriteId, Identifier atlasId) {

        TextureManagerAccessor textureManagerAccessor = (TextureManagerAccessor) Minecraft.getInstance().getTextureManager();
        DataResult<Identifier> atlasResult = tryLoadingTexture(atlasId, _id -> "Texture \"" + _id + "\" does not exist!", _id -> "\n");

        if (atlasResult.result().isEmpty()) {
            return atlasResult.mapError(err -> "Failed to load atlas \"%s\": %s".formatted(atlasId, err));
        }

        AbstractTexture texture = textureManagerAccessor.getByPath().get(atlasId);
        try {

            if (!(texture instanceof TextureAtlas atlasTexture)) {
                throw new IllegalArgumentException("Identifier \"" + atlasId + "\" does not refer to an atlas texture!");
            }

            TextureAtlasSprite MissingTextureAtlasSprite = atlasTexture.missingSprite();
            TextureAtlasSprite sprite = atlasTexture.getSprite(spriteId);

            if (sprite == MissingTextureAtlasSprite) {
                throw new IllegalArgumentException("TextureAtlasSprite \"" + spriteId + "\" does not exist in atlas \"" + atlasId + "\"!");
            }

            return DataResult.success(spriteId);

        } catch (Throwable t) {
            return DataResult.error(() -> "Failed to load sprite \"%s\" from atlas \"%s\": %s".formatted(spriteId, atlasId, t));
        }

    }

}
