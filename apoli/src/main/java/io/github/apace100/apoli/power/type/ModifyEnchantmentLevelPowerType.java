package io.github.apace100.apoli.power.type;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.access.EntityLinkedItemStack;
import io.github.apace100.apoli.command.argument.PowerHolderArgumentType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.mixin.LivingEntityAccessor;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.util.WorkableEmptyStack;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static net.minecraft.commands.Commands.*;

//  FIXME:  Some enchantments, notably, those that apply attribute modifiers when equipped, do not work properly with
//          this power type
public class ModifyEnchantmentLevelPowerType extends ValueModifyingPowerType {

    private static final Cache<UUID, Cache<ItemStack, ItemEnchantments>> ENCHANTMENTS_CACHE = CacheBuilder.newBuilder()
        .weakKeys()
        .build();

    public static final TypedDataObjectFactory<ModifyEnchantmentLevelPowerType> DATA_FACTORY = createConditionedModifyingRequiredDataFactory(
        new SerializableData()
            .add("enchantment", SerializableDataTypes.ENCHANTMENT)
            .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty()),
        (data, modifiers, condition) -> new ModifyEnchantmentLevelPowerType(
            data.get("enchantment"),
            data.get("item_condition"),
            modifiers,
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("enchantment", powerType.enchantmentKey)
            .set("item_condition", powerType.itemCondition)
    );

    private final Cache<ItemStack, StateHolder> stateCache = CacheBuilder.newBuilder()
        .weakKeys()
        .build();

    private final ResourceKey<Enchantment> enchantmentKey;
    private final Optional<ItemCondition> itemCondition;

    public ModifyEnchantmentLevelPowerType(ResourceKey<Enchantment> enchantmentKey, Optional<ItemCondition> itemCondition, List<Modifier> modifiers, Optional<EntityCondition> condition) {
        super(modifiers, condition);
        this.enchantmentKey = enchantmentKey;
        this.itemCondition = itemCondition;
        this.setTicking();
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return PowerTypes.MODIFY_ENCHANTMENT_LEVEL;
    }

    @Override
    public void onRemoved() {

        LivingEntity holder = getHolder();
        UUID uuid = holder.getUUID();

        for (var equipmentSlot : EquipmentSlot.values()) {

            ItemStack equippedStack = holder.getItemBySlot(equipmentSlot);

            if (WorkableEmptyStack.isOf(equippedStack)) {
                holder.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }

        }

        WorkableEmptyStack.remove(uuid);
        ENCHANTMENTS_CACHE.invalidate(uuid);

        stateCache.invalidateAll();

    }

    @Override
    public void serverTick() {

        LivingEntity holder = getHolder();

        for (var equipmentSlot : EquipmentSlot.values()) {

            SlotAccess stackReference = LivingEntityAccessor.callCreateEquipmentSlotAccess(holder, equipmentSlot);
            ItemStack stack = stackReference.get();

            if (stackReference != SlotAccess.of(null,null) && stack.isEmpty() && !WorkableEmptyStack.isOf(stack)) {
                stackReference.set(WorkableEmptyStack.getOrCreate(holder));
            }

        }

    }

    public boolean doesApply(ItemStack stack) {
        return this.isActive()
            && this.doesItemConditionApply(stack);
    }

    public boolean doesItemConditionApply(ItemStack stack) {
        return this.itemCondition
            .map(condition -> condition.test(getHolder().level(), stack))
            .orElse(true);
    }

    public boolean hasAppliedToStack(ItemStack stack) {
        var cache = stateCache.getIfPresent(stack);
        return cache != null
            && cache.applies;
    }

    public static ItemEnchantments getEnchantments(ItemStack stack, boolean modified) {
        return getEnchantmentsOrElse(stack, stack.getEnchantments(), modified);
    }

    @ApiStatus.Internal
    public static ItemEnchantments getEnchantmentsOrElse(ItemStack stack, ItemEnchantments defaultEnchantments, boolean modified) {

        if (!modified || !(((Object) stack) instanceof EntityLinkedItemStack linkedStack) || linkedStack.apoli$getEntity() == null) {
            return defaultEnchantments;
        }

        UUID uuid = linkedStack.apoli$getEntity().getUUID();
        var enchantmentsCache = ENCHANTMENTS_CACHE.getIfPresent(uuid);

        if (enchantmentsCache != null) {
            return enchantmentsCache.asMap().getOrDefault(stack, defaultEnchantments);
        }

        else {
            return defaultEnchantments;
        }

    }

    @ApiStatus.Internal
    public static ItemEnchantments updateAndGetEnchantments(ItemStack stack, ItemEnchantments original) {

        if (((Object) stack) instanceof EntityLinkedItemStack linkedStack && linkedStack.apoli$getEntity() instanceof LivingEntity livingEntity) {
            recalculateCache(livingEntity, stack);
        }

        return getEnchantmentsOrElse(stack, original, true);

    }

    /**
     *  Move the enchantments cache of an {@linkplain ItemStack item stack} to another, usually its copy.
     *  @param fromStack the {@linkplain ItemStack item stack} whose enchantments cache will be taken from
     *  @param toStack the {@linkplain ItemStack item stack} to move the enchantments cache to
     *  @return {@code toStack} with the updated enchantments cache
     */
    @ApiStatus.Internal
    public static ItemStack moveCache(ItemStack fromStack, ItemStack toStack) {

        if (!(((Object) fromStack) instanceof EntityLinkedItemStack linkedStack) || linkedStack.apoli$getEntity() == null) {
            return toStack;
        }

        Entity entity = linkedStack.apoli$getEntity();
        var enchantmentsMapCache = ENCHANTMENTS_CACHE.getIfPresent(entity.getUUID());

        if (!PowerHolderComponent.getPowerTypes(entity, ModifyEnchantmentLevelPowerType.class, true).isEmpty()) {

            if (toStack.isEmpty()) {
                toStack = WorkableEmptyStack.getOrCreate(entity);
            }

            else {
                ((EntityLinkedItemStack) (Object) toStack).apoli$setEntity(entity);
            }

        }

        if (enchantmentsMapCache != null) {

            var enchantmentsCache = enchantmentsMapCache.asMap().remove(fromStack);
            var powerComponents = PowerHolderComponent.getNullable(entity);

            if (enchantmentsCache != null) {
                enchantmentsMapCache.put(toStack, enchantmentsCache);
            }

            if (powerComponents != null) {
                powerComponents.getPowerTypes(ModifyEnchantmentLevelPowerType.class, true).forEach(powerType -> powerType.stateCache.invalidate(fromStack));
            }

        }

        return toStack;

    }

    public static void recalculateCache(LivingEntity entity, ItemStack stack) {

        UUID uuid = entity.getUUID();
        boolean update = false;

        //  Iterate through each power type without checking if it's active...
        for (var powerType : PowerHolderComponent.getPowerTypes(entity, ModifyEnchantmentLevelPowerType.class, true)) {

            //  ...to check if the power type no longer applies to the item stack, or if its modifiers have changed
            var newCache = new StateHolder(powerType.doesApply(stack), (int) Math.round(ModifierUtil.applyModifiers(entity, powerType.getModifiers(), 0)));
            var oldCache = powerType.stateCache.getIfPresent(stack);

            //  If the power type doesn't have a cache of its previous state, or its previous and current state no longer match...
            if (oldCache != null && oldCache.equals(newCache)) {
                continue;
            }

            //  ...update the state cache and state that the enchantments cache has to be updated as well
            powerType.stateCache.put(stack, newCache);
            update = true;

        }

        //  If the enchantments cache should be updated...
        if (!update) {
            return;
        }

        Registry<Enchantment> enchantmentRegistry = entity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments.Mutable enchantmentsBuilder = new ItemEnchantments.Mutable(stack.getEnchantments());

        IntSet processedEnchantmentIds = new IntOpenHashSet();
        update = false;

        //  ...iterate through every power type without checking if it's active again (to eliminate unnecessary checks)
        for (var powerType : PowerHolderComponent.getPowerTypes(entity, ModifyEnchantmentLevelPowerType.class, true)) {

            Holder.Reference<Enchantment> enchantmentReference = enchantmentRegistry.getOrThrow(powerType.enchantmentKey);
            int enchantmentId = enchantmentRegistry.getId(enchantmentReference.value());

            //  If the specified enchantment in the power type hasn't been processed yet...
            if (processedEnchantmentIds.contains(enchantmentId)) {
                continue;
            }

            //  ...modify the level of the enchantment by checking if the power type's previously updated state applies
            //  to the stack and if it matches the specified enchantment in the power type...
            int level = enchantmentsBuilder.getLevel(enchantmentReference);
            int modifiedLevel = (int) Math.round(PowerHolderComponent.modify(entity, ModifyEnchantmentLevelPowerType.class, level, innerPowerType -> innerPowerType.hasAppliedToStack(stack) && innerPowerType.enchantmentKey.equals(powerType.enchantmentKey), innerPowerType -> {}, true));

            enchantmentsBuilder.set(enchantmentReference, modifiedLevel);
            processedEnchantmentIds.add(enchantmentId);

            //  ...and state that the enchantments cache has been updated
            update = true;

        }

        if (update) {

	        try {
		        ENCHANTMENTS_CACHE
		            .get(uuid, () -> CacheBuilder.newBuilder().weakKeys().build())
		            .put(stack, enchantmentsBuilder.toImmutable());
	        }

            catch (ExecutionException e) {
                //  This shouldn't happen as there isn't any exceptions thrown when loading new cache values
                Apoli.LOGGER.warn("Failed to update enchantments cache!", e);
	        }

        }

    }

    public static final class DebugCommand {

	    public static void register(RootCommandNode<CommandSourceStack> rootNode) {

            var melNode = literal(PowerTypes.MODIFY_ENCHANTMENT_LEVEL.id().getPath())
                .requires(source -> LEVEL_GAMEMASTERS.check(source.permissions()))
                .build();

            melNode.addChild(CacheNode.builder().build());
            rootNode.addChild(melNode);

	    }

        public static class CacheNode {

            public static LiteralArgumentBuilder<CommandSourceStack> builder() {
                return literal("cache")
                    .then(argument("target", PowerHolderArgumentType.holder())
                        .executes(CacheNode::execute));
            }

            public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

                UUID uuid = PowerHolderArgumentType.getHolder(context, "target").getUUID();
                var enchantmentsMapCache = ENCHANTMENTS_CACHE.getIfPresent(uuid);

                if (enchantmentsMapCache == null) {
                    throw new SimpleCommandExceptionType(() -> "Entity with UUID \"" + uuid + "\" didn't have a cache of enchantments!").create();
                }

                MutableComponent enchantmentsText = Component.empty();
                var enchantmentsEntrySet = enchantmentsMapCache.asMap().entrySet();

                for (var entry : enchantmentsEntrySet) {

                    var stack = entry.getKey();
                    ItemEnchantments enchantments = entry.getValue();

                    enchantmentsText
                        .append("\n")
                        .append(Component.literal(stack.toString() + " (identity hash: " + stack.hashCode() + ")"));

                    for (var enchantmentWithLevel : enchantments.entrySet()) {

                        var enchantment = enchantmentWithLevel.getKey();
                        var level = enchantmentWithLevel.getIntValue();

                        enchantmentsText
                            .append("\n")
                            .append(" - (").append(enchantment.value().description()).append("): ")
                            .append(Component.literal(Integer.toString(level)));

                    }

                }

                context.getSource().sendSuccess(() -> enchantmentsText, false);
                return (int) enchantmentsMapCache.size();

            }

        }

    }

    public record StateHolder(boolean applies, int level) {

    }

}
