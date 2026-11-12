//package io.github.apace100.apoli.action.type.entity;
//
//import io.github.apace100.apoli.access.ScreenHandlerUsabilityOverride;
//import io.github.apace100.apoli.action.ActionConfiguration;
//import io.github.apace100.apoli.action.context.EntityActionContext;
//import io.github.apace100.apoli.action.type.EntityActionType;
//import io.github.apace100.apoli.action.type.EntityActionTypes;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.screen.CraftingScreenHandler;
//import net.minecraft.screen.ScreenHandlerContext;
//import net.minecraft.world.inventory.MenuConstructor;
//import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
//import net.minecraft.stat.Stats;
//import net.minecraft.network.chat.Component;
//import org.jetbrains.annotations.NotNull;
//
//public class CraftingTableEntityActionType extends EntityActionType {
//
//    @Override
//    public void accept(EntityActionContext context) {
//
//        if (context.entity() instanceof Player player) {
//
//            MenuConstructor handlerFactory = (syncId, playerInventory, _player) -> {
//
//                CraftingScreenHandler craftingScreenHandler = new CraftingScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(player.level(), player.getBlockPos()));
//                ((ScreenHandlerUsabilityOverride) craftingScreenHandler).apoli$canUse(true);
//
//                return craftingScreenHandler;
//
//            };
//
//            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(handlerFactory, Component.translatable("container.crafting")));
//            player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
//
//        }
//
//    }
//
//    @Override
//    public @NotNull ActionConfiguration<?> getConfig() {
//        return EntityActionTypes.CRAFTING_TABLE;
//    }
//
//}
