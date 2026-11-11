//package io.github.apace100.apoli.power.type;
//
//import io.github.apace100.apoli.condition.EntityCondition;
//import net.minecraft.entity.effect.MobEffect;
//import net.minecraft.entity.effect.MobEffectInstance;
//import net.minecraft.registry.entry.Holder;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//
//public abstract class MobEffectPowerType extends PowerType {
//
//    protected final List<MobEffectInstance> effects = new LinkedList<>();
//
//    public MobEffectPowerType(List<MobEffectInstance> effectInstances, Optional<EntityCondition> condition) {
//        super(condition);
//        this.effects.addAll(effectInstances);
//    }
//
//    public MobEffectPowerType(Optional<EntityCondition> condition) {
//        super(condition);
//    }
//
//    public MobEffectPowerType() {
//
//    }
//
//    public MobEffectPowerType(MobEffectInstance effectInstance, Optional<EntityCondition> condition) {
//        this(condition);
//        this.addEffect(effectInstance);
//    }
//
//    public MobEffectPowerType(MobEffectInstance effectInstance) {
//        this(effectInstance, Optional.empty());
//    }
//
//    public void addEffect(Holder<MobEffect> effect, int duration) {
//        addEffect(effect, duration, 0);
//    }
//
//    public void addEffect(Holder<MobEffect> effect, int duration, int amplifier) {
//        addEffect(new MobEffectInstance(effect, duration, amplifier));
//    }
//
//    public void addEffect(MobEffectInstance instance) {
//        effects.add(instance);
//    }
//
//    public void applyEffects() {
//        effects.stream().map(MobEffectInstance::new).forEach(getHolder()::addMobEffect);
//    }
//
//}
