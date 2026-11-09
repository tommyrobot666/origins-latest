package io.github.apace100.origins.mixin;


import io.github.apace100.origins.power.type.ScareCreepersPowerType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class ScareCreepersMixin extends LivingEntity  {

    private ScareCreepersMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;registerGoals()V", shift = At.Shift.AFTER))
    private void origins$modifyGoals(EntityType<?> entityType, Level world, CallbackInfo ci) {

        if ((Mob) (Object) this instanceof Creeper thisAsCreeper) {
            ScareCreepersPowerType.modifyGoals(thisAsCreeper);
        }

    }

}
