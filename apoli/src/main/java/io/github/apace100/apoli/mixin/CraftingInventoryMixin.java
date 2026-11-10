//package io.github.apace100.apoli.mixin;
//
//import io.github.apace100.apoli.access.PowerCraftingInventory;
//import io.github.apace100.apoli.power.type.PowerType;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.inventory.CraftingInventory;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//
//import java.util.Collection;
//import java.util.LinkedList;
//
//@Mixin(CraftingInventory.class)
//public abstract class CraftingInventoryMixin implements PowerCraftingInventory {
//
//    @Unique
//    private Collection<? extends PowerType> apoli$CachedPowerTypes = new LinkedList<>();
//
//    @Unique
//    private Player apoli$cachedPlayer;
//
//    @Override
//    public Collection<? extends PowerType> apoli$getPowerTypes() {
//        return apoli$CachedPowerTypes;
//    }
//
//    @Override
//    public void apoli$setPowerTypes(Collection<? extends PowerType> powerTypes) {
//        apoli$CachedPowerTypes = powerTypes;
//    }
//
//    @Override
//    public CraftingInventory apoli$getInventory() {
//        return (CraftingInventory) (Object) this;
//    }
//
//    @Override
//    public Player apoli$getPlayer() {
//        return apoli$cachedPlayer;
//    }
//
//    @Override
//    public void apoli$setPlayer(Player player) {
//        this.apoli$cachedPlayer = player;
//    }
//
//}
