package io.github.apace100.apoli.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.access.PowerCraftingObject;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import io.github.apace100.apoli.power.type.RecipePowerType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public record PowerCraftingRecipe(Identifier powerId, CraftingRecipe delegate) implements CraftingRecipe {

    @Override
    public CraftingBookCategory category() {
        return delegate().category();
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {

        if (!(input instanceof PowerCraftingObject pco)) {
            return false;
        }

        Power power = PowerManager.getNullable(powerId());
        PowerHolderComponent component = PowerHolderComponent.getNullable(pco.apoli$getPlayer());

        return power != null
            && component != null
            && component.hasPower(power)
            && component.getPowerType(power) instanceof RecipePowerType
            && delegate().matches(input, world);

    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return delegate().assemble(input);
    }

//    @Override
//    public boolean fits(int width, int height) {
//        return delegate().fits(width, height);
//    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return ApoliRecipeSerializers.POWER_CRAFTING;
    }

    @Override
    public PlacementInfo placementInfo() {
        return delegate().placementInfo();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return delegate().getRemainingItems(input);
    }

    @Override
    public boolean isSpecial() {
        return delegate().isSpecial();
    }

    @Override
    public boolean showNotification() {
        return delegate().showNotification();
    }

    @Override
    public String group() {
        return delegate().group();
    }

    private static void send(RegistryFriendlyByteBuf buf, PowerCraftingRecipe recipe) {
        buf.writeIdentifier(recipe.powerId());
        Recipe.STREAM_CODEC.encode(buf, recipe.delegate());
    }

    private static PowerCraftingRecipe receive(RegistryFriendlyByteBuf buf) {

        Identifier powerId = buf.readIdentifier();
        Recipe<?> recipe = Recipe.STREAM_CODEC.decode(buf);

        if (recipe instanceof CraftingRecipe craftingRecipe) {
            return new PowerCraftingRecipe(powerId, craftingRecipe);
        }

        else {
            throw new IllegalStateException("Recipe is not a crafting recipe!");
        }

    }

    public static class Serializer  {

        public static final MapCodec<PowerCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("power").forGetter(PowerCraftingRecipe::powerId),
            ApoliDataTypes.DISALLOWING_INTERNAL_CRAFTING_RECIPE.codec().fieldOf("recipe").forGetter(PowerCraftingRecipe::delegate)
        ).apply(instance, PowerCraftingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PowerCraftingRecipe> STREAM_CODEC = StreamCodec.of(
            PowerCraftingRecipe::send,
            PowerCraftingRecipe::receive
        );

       public static final RecipeSerializer<PowerCraftingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC,STREAM_CODEC);
    }

}
