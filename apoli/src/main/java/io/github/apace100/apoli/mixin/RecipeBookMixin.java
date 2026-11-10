//package io.github.apace100.apoli.mixin;
//
//import io.github.apace100.apoli.access.PowerCraftingObject;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.recipe.book.RecipeBook;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//
//import java.lang.ref.WeakReference;
//
//@Mixin(RecipeBook.class)
//public abstract class RecipeBookMixin implements PowerCraftingObject {
//
//    @Unique
//    private WeakReference<Player> apoli$player;
//
//    @Nullable
//    @Override
//    public Player apoli$getPlayer() {
//
//        if (apoli$player != null) {
//            return apoli$player.get();
//        }
//
//        else {
//            return null;
//        }
//
//    }
//
//    @Override
//    public void apoli$setPlayer(Player player) {
//
//        if (player == null) {
//            this.apoli$player = null;
//        }
//
//        else {
//            this.apoli$player = new WeakReference<>(player);
//        }
//
//    }
//
//}
