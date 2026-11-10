//package io.github.apace100.apoli.recipe;
//
//import com.mojang.datafixers.util.Pair;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import io.github.apace100.apoli.access.PowerCraftingInventory;
//import io.github.apace100.apoli.access.PowerCraftingObject;
//import io.github.apace100.apoli.component.PowerHolderComponent;
//import io.github.apace100.apoli.data.ApoliDataTypes;
//import io.github.apace100.apoli.mixin.CraftingInventoryAccessor;
//import io.github.apace100.apoli.mixin.CraftingScreenHandlerAccessor;
//import io.github.apace100.apoli.power.type.ModifyCraftingPowerType;
//import io.github.apace100.apoli.power.type.Prioritized;
//import io.github.apace100.apoli.util.InventoryUtil;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.inventory.CraftingInventory;
//import net.minecraft.inventory.StackReference;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.recipe.CraftingRecipe;
//import net.minecraft.recipe.Ingredient;
//import net.minecraft.recipe.Recipe;
//import net.minecraft.recipe.RecipeSerializer;
//import net.minecraft.recipe.book.CraftingRecipeCategory;
//import net.minecraft.recipe.book.RecipeBook;
//import net.minecraft.recipe.input.CraftingRecipeInput;
//import net.minecraft.recipe.input.RecipeInput;
//import net.minecraft.registry.RegistryWrapper;
//import net.minecraft.screen.CraftingScreenHandler;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Collection;
//import java.util.Optional;
//
//public record ModifiedCraftingRecipe(Identifier id, CraftingRecipe delegate) implements CraftingRecipe {
//
//    @Override
//    public CraftingRecipeCategory getCategory() {
//        return delegate().getCategory();
//    }
//
//    @Override
//    public boolean matches(CraftingRecipeInput input, World world) {
//        return delegate().matches(input, world);
//    }
//
//    @Override
//    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
//
//        if (input instanceof PowerCraftingInventory pci) {
//
//            Pair<ItemStack, Collection<ModifyCraftingPowerType>> result = this.getModifiedResult(lookup, pci.apoli$getPlayer());
//            pci.apoli$setPowerTypes(result.getSecond());
//
//            return result.getFirst().copy();
//
//        }
//
//        else {
//            return this.getResult(lookup).copy();
//        }
//
//    }
//
//    @Override
//    public boolean fits(int width, int height) {
//        return delegate().fits(width, height);
//    }
//
//    @Override
//    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
//        return delegate().getResult(registriesLookup);
//    }
//
//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return ApoliRecipeSerializers.MODIFIED_CRAFTING;
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
//    public Pair<ItemStack, Collection<ModifyCraftingPowerType>> getModifiedResult(RegistryWrapper.WrapperLookup registriesLookup, @Nullable Player player) {
//        return getModifiedResult(id(), delegate(), registriesLookup, player);
//    }
//
//    public static boolean canModify(Identifier id, CraftingRecipe craftingRecipe, RecipeBook recipeBook) {
//        return recipeBook instanceof PowerCraftingObject pco
//            && canModify(id, craftingRecipe, pco.apoli$getPlayer());
//    }
//
//    public static boolean canModify(Identifier id, CraftingRecipe craftingRecipe, RecipeInput recipeInput) {
//        return recipeInput instanceof PowerCraftingObject pco
//            && canModify(id, craftingRecipe, pco.apoli$getPlayer());
//    }
//
//    public static boolean canModify(Identifier id, CraftingRecipe craftingRecipe, @Nullable Player player) {
//        return player != null
//            && PowerHolderComponent.hasPowerType(player, ModifyCraftingPowerType.class, mcpt -> mcpt.doesApply(id, craftingRecipe.getResult(player.getRegistryManager())));
//    }
//
//    public static Pair<ItemStack, Collection<ModifyCraftingPowerType>> getModifiedResult(Identifier id, CraftingRecipe craftingRecipe, RegistryWrapper.WrapperLookup registriesLookup, @Nullable Player player) {
//
//        ItemStack resultStack = craftingRecipe.getResult(registriesLookup).copy();
//        StackReference newStackRef = InventoryUtil.createStackReference(resultStack);
//
//        Prioritized.CallInstance<ModifyCraftingPowerType> mcptpci = new Prioritized.CallInstance<>();
//        mcptpci.add(player, ModifyCraftingPowerType.class, mcpt -> mcpt.doesApply(id, resultStack));
//
//        for (int i = mcptpci.getMaxPriority(); i >= mcptpci.getMinPriority(); i--) {
//            mcptpci.getPowerTypes(i).forEach(mcpt -> mcpt.getNewResult(newStackRef));
//        }
//
//        return Pair.of(newStackRef.get(), mcptpci.getAllPowerTypes());
//
//    }
//
//    public static Optional<BlockPos> getBlockFromInventory(CraftingInventory craftingInventory) {
//
//        if (((CraftingInventoryAccessor) craftingInventory).getHandler() instanceof CraftingScreenHandler craftingScreenHandler) {
//            return ((CraftingScreenHandlerAccessor) craftingScreenHandler).getContext().get((world, pos) -> pos);
//        }
//
//        else {
//            return Optional.empty();
//        }
//
//    }
//
//    private void send(RegistryFriendlyByteBuf buf) {
//        buf.writeIdentifier(id());
//        Recipe.STREAM_CODEC.encode(buf, delegate());
//    }
//
//    private static ModifiedCraftingRecipe receive(RegistryFriendlyByteBuf buf) {
//
//        Identifier id = buf.readIdentifier();
//        Recipe<?> recipe = Recipe.STREAM_CODEC.decode(buf);
//
//        if (recipe instanceof CraftingRecipe craftingRecipe) {
//            return new ModifiedCraftingRecipe(id, craftingRecipe);
//        }
//
//        else {
//            throw new IllegalStateException("Recipe is not a crafting recipe!");
//        }
//
//    }
//
//    public static class Serializer implements RecipeSerializer<ModifiedCraftingRecipe> {
//
//        public static final MapCodec<ModifiedCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
//            Identifier.CODEC.fieldOf("id").forGetter(ModifiedCraftingRecipe::id),
//            ApoliDataTypes.DISALLOWING_INTERNAL_CRAFTING_RECIPE.codec().fieldOf("recipe").forGetter(ModifiedCraftingRecipe::delegate)
//        ).apply(instance, ModifiedCraftingRecipe::new));
//
//        public static final StreamCodec<RegistryFriendlyByteBuf, ModifiedCraftingRecipe> STREAM_CODEC = StreamCodec.of(
//            ModifiedCraftingRecipe::send,
//            ModifiedCraftingRecipe::receive
//        );
//
//        @Override
//        public MapCodec<ModifiedCraftingRecipe> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, ModifiedCraftingRecipe> packetCodec() {
//            return STREAM_CODEC;
//        }
//
//    }
//
//}
