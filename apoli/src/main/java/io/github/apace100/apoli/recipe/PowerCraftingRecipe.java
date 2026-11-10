//package io.github.apace100.apoli.recipe;
//
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import io.github.apace100.apoli.access.PowerCraftingObject;
//import io.github.apace100.apoli.component.PowerHolderComponent;
//import io.github.apace100.apoli.data.ApoliDataTypes;
//import io.github.apace100.apoli.power.Power;
//import io.github.apace100.apoli.power.PowerManager;
//import io.github.apace100.apoli.power.type.RecipePowerType;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.recipe.CraftingRecipe;
//import net.minecraft.recipe.Ingredient;
//import net.minecraft.recipe.Recipe;
//import net.minecraft.recipe.RecipeSerializer;
//import net.minecraft.recipe.book.CraftingRecipeCategory;
//import net.minecraft.recipe.input.CraftingRecipeInput;
//import net.minecraft.registry.RegistryWrapper;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.world.Level;
//
//public record PowerCraftingRecipe(Identifier powerId, CraftingRecipe delegate) implements CraftingRecipe {
//
//    @Override
//    public CraftingRecipeCategory getCategory() {
//        return delegate().getCategory();
//    }
//
//    @Override
//    public boolean matches(CraftingRecipeInput input, Level world) {
//
//        if (!(input instanceof PowerCraftingObject pco)) {
//            return false;
//        }
//
//        Power power = PowerManager.getNullable(powerId());
//        PowerHolderComponent component = PowerHolderComponent.getNullable(pco.apoli$getPlayer());
//
//        return power != null
//            && component != null
//            && component.hasPower(power)
//            && component.getPowerType(power) instanceof RecipePowerType
//            && delegate().matches(input, world);
//
//    }
//
//    @Override
//    public ItemStack craft(CraftingRecipeInput input, HolderLookup.Provider lookup) {
//        return delegate().craft(input, lookup);
//    }
//
//    @Override
//    public boolean fits(int width, int height) {
//        return delegate().fits(width, height);
//    }
//
//    @Override
//    public ItemStack getResult(HolderLookup.Provider registriesLookup) {
//        return delegate().getResult(registriesLookup);
//    }
//
//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return ApoliRecipeSerializers.POWER_CRAFTING;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return delegate().isEmpty();
//    }
//
//    @Override
//    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
//        return delegate().getRemainder(input);
//    }
//
//    @Override
//    public DefaultedList<Ingredient> getIngredients() {
//        return delegate().getIngredients();
//    }
//
//    @Override
//    public boolean isIgnoredInRecipeBook() {
//        return delegate().isIgnoredInRecipeBook();
//    }
//
//    @Override
//    public boolean showNotification() {
//        return delegate().showNotification();
//    }
//
//    @Override
//    public String getGroup() {
//        return delegate().getGroup();
//    }
//
//    private void send(RegistryFriendlyByteBuf buf) {
//        buf.writeIdentifier(powerId());
//        Recipe.STREAM_CODEC.encode(buf, delegate());
//    }
//
//    private static PowerCraftingRecipe receive(RegistryFriendlyByteBuf buf) {
//
//        Identifier powerId = buf.readIdentifier();
//        Recipe<?> recipe = Recipe.STREAM_CODEC.decode(buf);
//
//        if (recipe instanceof CraftingRecipe craftingRecipe) {
//            return new PowerCraftingRecipe(powerId, craftingRecipe);
//        }
//
//        else {
//            throw new IllegalStateException("Recipe is not a crafting recipe!");
//        }
//
//    }
//
//    public static class Serializer implements RecipeSerializer<PowerCraftingRecipe> {
//
//        public static final MapCodec<PowerCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
//            Identifier.CODEC.fieldOf("power").forGetter(PowerCraftingRecipe::powerId),
//            ApoliDataTypes.DISALLOWING_INTERNAL_CRAFTING_RECIPE.codec().fieldOf("recipe").forGetter(PowerCraftingRecipe::delegate)
//        ).apply(instance, PowerCraftingRecipe::new));
//
//        public static final StreamCodec<RegistryFriendlyByteBuf, PowerCraftingRecipe> STREAM_CODEC = StreamCodec.of(
//            PowerCraftingRecipe::send,
//            PowerCraftingRecipe::receive
//        );
//
//        @Override
//        public MapCodec<PowerCraftingRecipe> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, PowerCraftingRecipe> packetCodec() {
//            return STREAM_CODEC;
//        }
//
//    }
//
//}
