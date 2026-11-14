package io.github.apace100.apoli.recipe;

import io.github.apace100.apoli.Apoli;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class ApoliRecipeTypes {
    public static <R extends Recipe<?>> RecipeType<R> register(String path) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, Apoli.identifier(path), new RecipeType<R>(){
            @Override
            public String toString() {
                return path;
            }
        });
    }

    public static final RecipeType<ModifiedCraftingRecipe> MODIFIED_CRAFTING_RECIPE = register("modified_crafting");
    public static final RecipeType<PowerCraftingRecipe> POWER_CRAFTING_RECIPE = register("power_crafting");

    public static void register() {}
}
