package io.github.apace100.apoli.recipe;

import io.github.apace100.apoli.Apoli;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.Registry;

public class ApoliRecipeSerializers {

    public static final RecipeSerializer<PowerCraftingRecipe> POWER_CRAFTING = register("power_crafting", PowerCraftingRecipe.Serializer.SERIALIZER);
    public static final RecipeSerializer<ModifiedCraftingRecipe> MODIFIED_CRAFTING = register("modified_crafting", ModifiedCraftingRecipe.Serializer.SERIALIZER);

    public static void register() {

    }

    public static <R extends Recipe<?>, S extends RecipeSerializer<R>> S register(String path, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Apoli.identifier(path), serializer);
    }

}
