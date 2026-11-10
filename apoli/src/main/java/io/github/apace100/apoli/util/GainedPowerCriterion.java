//package io.github.apace100.apoli.util;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.power.Power;
//import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
//import net.minecraft.core.Holder;
//import net.minecraft.resources.Identifier;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
//
//import java.util.Optional;
//
//public class GainedPowerCriterion extends SimpleCriterionTrigger<GainedPowerCriterion.Conditions> {
//
//    public static final GainedPowerCriterion INSTANCE = new GainedPowerCriterion();
//    public static final Identifier ID = Apoli.identifier("gained_power");
//
//    public void trigger(ServerPlayer player, Power power) {
//        this.trigger(player, conditions -> conditions.matches(power));
//    }
//
//    @Override
//    public Codec<Conditions> codec() {
//        return Conditions.CODEC;
//    }
//
//    public record Conditions(Optional<Holder<LootItemCondition>> player, Identifier powerId) implements SimpleCriterionTrigger.SimpleInstance {
//
//        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
//            Identifier.CODEC.fieldOf("power").forGetter(Conditions::powerId)
//        ).apply(instance, Conditions::new));
//
//        @Override
//        public Optional<Holder<LootItemCondition>> player() {
//            return player;
//        }
//
//        public boolean matches(Power power) {
//            return power.getId().equals(powerId);
//        }
//
//    }
//
//}
