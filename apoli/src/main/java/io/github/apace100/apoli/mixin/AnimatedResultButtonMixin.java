//package io.github.apace100.apoli.mixin;
//
//import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.llamalad7.mixinextras.sugar.Share;
//import com.llamalad7.mixinextras.sugar.ref.LocalRef;
//import io.github.apace100.apoli.access.PowerCraftingObject;
//import io.github.apace100.apoli.component.PowerHolderComponent;
//import io.github.apace100.apoli.power.Power;
//import io.github.apace100.apoli.power.PowerManager;
//import io.github.apace100.apoli.recipe.ModifiedCraftingRecipe;
//import io.github.apace100.apoli.recipe.PowerCraftingRecipe;
//import net.minecraft.client.gui.screen.recipebook.AnimatedResultButton;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.CraftingRecipe;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.recipe.RecipeHolder;
//import net.minecraft.recipe.book.RecipeBook;
//import net.minecraft.registry.RegistryWrapper;
//import net.minecraft.network.chat.Component;
//import net.minecraft.util.ChatFormatting;
//import net.minecraft.resources.Identifier;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//
//import java.util.List;
//
//@Mixin(AnimatedResultButton.class)
//public abstract class AnimatedResultButtonMixin {
//
//    @Shadow
//    public abstract RecipeHolder<?> currentRecipe();
//
//    @Shadow
//    private RecipeBook recipeBook;
//
//    @WrapOperation(method = {"renderWidget", "getTooltip", "appendClickableNarrations"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeHolder;value()Lnet/minecraft/recipe/Recipe;"))
//    private Recipe<?> apoli$modifyEntryQuery(RecipeHolder<?> entry, Operation<Recipe<?>> original, @Share("originalEntry") LocalRef<RecipeHolder<?>> sharedOriginalEntry) {
//
//        sharedOriginalEntry.set(entry);
//
//        Identifier id = entry.id();
//        Recipe<?> recipe = entry.value();
//
//        if (recipe instanceof CraftingRecipe craftingRecipe && ModifiedCraftingRecipe.canModify(id, craftingRecipe, this.recipeBook)) {
//            return new ModifiedCraftingRecipe(id, craftingRecipe);
//        }
//
//        else {
//            return original.call(entry);
//        }
//
//    }
//
//    @WrapOperation(method = {"renderWidget", "getTooltip", "appendClickableNarrations"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;getResult(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"))
//    private ItemStack apoli$modifyResultQuery(Recipe<?> recipe, HolderLookup.Provider wrapperLookup, Operation<ItemStack> original) {
//
//        if (recipe instanceof ModifiedCraftingRecipe modifiedCraftingRecipe && this.recipeBook instanceof PowerCraftingObject pco) {
//            return modifiedCraftingRecipe.getModifiedResult(wrapperLookup, pco.apoli$getPlayer()).getFirst();
//        }
//
//        else {
//            return original.call(recipe, wrapperLookup);
//        }
//
//    }
//
//    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
//    private List<Component> apoli$appendRequiredRecipePowerTooltip(List<Component> original, @Share("originalEntry") LocalRef<RecipeHolder<?>> sharedOriginalEntry) {
//
//        RecipeHolder<?> recipeEntry = sharedOriginalEntry.get() != null
//            ? sharedOriginalEntry.get()
//            : this.currentRecipe();
//
//        if (recipeEntry.value() instanceof PowerCraftingRecipe pcr && this.recipeBook instanceof PowerCraftingObject pco) {
//
//            Power power = PowerManager.getNullable(pcr.powerId());
//            PowerHolderComponent component = PowerHolderComponent.getNullable(pco.apoli$getPlayer());
//
//            if (power != null && component != null && !component.hasPower(power)) {
//                original.add(Component.empty());
//                original.add(Component.translatable("tooltip.apoli.power_recipe.required_power", power.getName()).withStyle(ChatFormatting.RED));
//            }
//
//        }
//
//        return original;
//
//    }
//
//}
