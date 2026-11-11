package io.github.apace100.calio;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class CodeTriggerCriterion extends SimpleCriterionTrigger<CodeTriggerCriterion.Conditions> {

    public static final CodeTriggerCriterion INSTANCE = new CodeTriggerCriterion();
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Calio.MOD_NAMESPACE,"apacelib:code_trigger");

    @Override
    public @NonNull Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player, String triggerId) {
        super.trigger(player, conditions -> conditions.matches(triggerId));
    }

    public record Conditions(Optional<Holder<LootItemCondition>> playerPredicate, Optional<String> triggerId) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
            Codec.STRING.optionalFieldOf("trigger_id").forGetter(Conditions::triggerId)
        ).apply(instance, Conditions::new));

        @Override
        public Optional<Holder<LootItemCondition>> player() {
            return playerPredicate;
        }

        public boolean matches(String triggerId) {
            return this.triggerId
                .map(triggerId::equals)
                .orElse(true);
        }

    }

}
