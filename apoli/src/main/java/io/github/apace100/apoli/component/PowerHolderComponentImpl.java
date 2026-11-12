package io.github.apace100.apoli.component;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.MultiplePower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.GainedPowerCriterion;
import io.github.apace100.calio.data.SerializableData;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.TagValueInput;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PowerHolderComponentImpl implements PowerHolderComponent {

    private final Map<Power, PowerType> powers = new Object2ObjectLinkedOpenHashMap<>();
    private final Map<Power, Set<Identifier>> powerSources = new Object2ObjectLinkedOpenHashMap<>();

    private final LivingEntity owner;

    public PowerHolderComponentImpl(LivingEntity owner) {
        this.owner = owner;
    }

    @Override
    public boolean hasPower(Power power) {
        return powers.containsKey(power);
    }

    @Override
    public boolean hasPower(Power power, Identifier source) {
        return powerSources.containsKey(power) && powerSources.get(power).contains(source);
    }

    @Override
    public PowerType getPowerType(Power power) {
        return powers.get(power);
    }

    @Override
    public List<PowerType> getPowerTypes() {
        return new LinkedList<>(powers.values());
    }

    @Override
    public Set<Power> getPowers(boolean includeSubPowers) {

        Set<Power> result = new ObjectLinkedOpenHashSet<>();

        for (var power : powers.keySet()) {

            if (includeSubPowers || !power.isSubPower()) {
                result.add(power);
            }

        }

        return result;

    }

    @Override
    public <T extends PowerType> List<T> getPowerTypes(Class<T> typeClass) {
        return getPowerTypes(typeClass, false);
    }

    @Override
    public <T extends PowerType> List<T> getPowerTypes(Class<T> typeClass, boolean includeInactive) {

        List<T> result = new ObjectArrayList<>();

        for (var type : powers.values()) {

            if (typeClass.isInstance(type)) {

                T castedType = typeClass.cast(type);

                if (includeInactive || castedType.isActive()) {
                    result.add(castedType);
                }

            }

        }

        return result;

    }

    @Override
    public List<Identifier> getSources(Power power) {

        if (powerSources.containsKey(power)) {
            return List.copyOf(powerSources.get(power));
        }

        else {
            return List.of();
        }

    }

    @Override
    public boolean removePower(Power power, Identifier source) {

        Set<Power> powersToRemove = new ObjectLinkedOpenHashSet<>();
        boolean result = this.removePower(power, source, powersToRemove::add);

        powers.keySet().removeAll(powersToRemove);
        powerSources.keySet().removeAll(powersToRemove);

        return result;

    }

    protected boolean removePower(Power power, Identifier source, Consumer<Power> adder) {

        Set<Identifier> sources = powerSources.getOrDefault(power, new ObjectOpenHashSet<>());
        if (!sources.remove(source)) {
            return false;
        }

        if (sources.isEmpty() && powers.containsKey(power)) {

            PowerType powerType = powers.get(power);
            adder.accept(power);

            powerType.onRemoved();
            powerType.onLost();

        }

        if (power instanceof MultiplePower multiplePower) {
            multiplePower.getSubPowers().forEach(subPower -> this.removePower(subPower, source, adder));
        }

        return true;

    }

    @Override
    public int removeAllPowersFromSource(Identifier source) {

        List<Power> powersFromSource = this.getPowersFromSource(source);
        int count = 0;

        for (var power : powersFromSource) {

            if (!power.isSubPower() && this.removePower(power, source)) {
                count++;
            }

        }

        return count;

    }

    @Override
    public List<Power> getPowersFromSource(Identifier source) {

        List<Power> result = new ObjectArrayList<>();

        for (var entry : powerSources.entrySet()) {

            if (entry.getValue().contains(source)) {
                result.add(entry.getKey());
            }

        }

        return result;

    }

    @Override
    public boolean addPower(Power power, Identifier source) {

        Map<Power, PowerType> powersToAdd = new Object2ObjectArrayMap<>();
        boolean result = this.addPower(power, source, powersToAdd::put);

        powersToAdd.forEach((powerToAdd, powerTypeToAdd) -> {

            powerTypeToAdd.onAdded();
            powerTypeToAdd.onGained();

            if (owner instanceof ServerPlayer serverPlayer) {
                GainedPowerCriterion.INSTANCE.trigger(serverPlayer, powerToAdd);
            }

        });

        return result;

    }

    protected boolean addPower(Power power, Identifier source, BiConsumer<Power, PowerType> adder) {

        Set<Identifier> sources = powerSources.computeIfAbsent(power, pt -> new ObjectOpenHashSet<>());
        if (!sources.add(source)) {
            return false;
        }

        PowerType powerType = shallowCopy(power.getType());

        powerType.setPower(power);
        powerType.setHolder(owner);

        powerType.onInit();
        adder.accept(power, powerType);

        powers.put(power, powerType);
        powerSources.put(power, sources);

        if (power instanceof MultiplePower multiplePower) {
            multiplePower.getSubPowers().forEach(subPower -> this.addPower(subPower, source, adder));
        }

        return true;

    }

    @Override
    public void tick() {

        Collection<PowerType> types = new ObjectArrayList<>(powers.values());
        boolean client = owner.level().isClientSide();

        for (var type : types) {

            if (type.shouldTick() && (type.shouldTickWhenInactive() || type.isActive())) {

                type.commonTick();

                if (client) {
                    type.clientTick();
                }

                else {
                    type.serverTick();
                }

            }

        }

    }

    @Override
    public void readData(ValueInput readView){

        powers.clear();
        powerSources.clear();

        ValueInput.ValueInputList powersTag = readView.childrenList("powers").orElseThrow();
        Stream<ValueInput> powersTagStream = powersTag.stream();
        List<ValueInput> powersTags;

        //  Migrate compound NBTs from the old 'Powers' NBT path to the new 'powers' NBT path
        if (readView.contains("Powers")) {
            powersTagStream = Stream.concat(powersTagStream,readView.childrenList("Powers").orElseThrow().stream());
        }

        powersTags = powersTagStream.toList();

        for (int i = 0; i < powersTags.size(); i++) {

            ValueInput powerTag = powersTags.get(i);

            try {

                Power.DataEntry powerDataEntry = Power.DataEntry.CODEC.read(NbtOps.INSTANCE, ((TagValueInput) powerTag).input).getOrThrow();
                PowerReference powerReference = powerDataEntry.powerReference();

                try {

                    Power power = powerReference.getPower();
                    PowerType powerType = shallowCopy(power.getType());

                    powerType.setPower(power);
                    powerType.setHolder(owner);

                    powerType.onInit();

                    try {
                        powerType.fromTag(powerDataEntry.nbtData());
                    }

                    catch (ClassCastException cce) {
                        //  Occurs when the power was overridden by a data pack since last resource reload,
                        //  where the overridden power may encode/decode different NBT types
                        Apoli.LOGGER.warn("Data type of power \"{}\" has changed, skipping data for that power on entity {} (UUID: {})", powerReference.id(), owner.getName().getString(), owner.getStringUUID());
                    }

                    powers.put(power, powerType);
                    powerSources.put(power, powerDataEntry.sources());

                }

                catch (Throwable t) {
                    Apoli.LOGGER.warn("Unregistered power \"{}\" found on entity {} (UUID: {}), skipping...", powerReference.id(), owner.getName().getString(), owner.getStringUUID());
                }

            }

            catch (Throwable t) {
                Apoli.LOGGER.warn("Error trying to decode NBT element ({}) at index {} into a power from NBT of entity {} (UUID: {}) (skipping): {}", powerTag, i, owner.getName().getString(), owner.getStringUUID(), t.getMessage());
            }

        }

    }

    @Override
    public void writeData(ValueOutput writeView) {

        ValueOutput.ValueOutputList powersTag = writeView.childrenList("powers");
        powers.forEach((power, powerType) -> {
            ValueOutput powerTag = powersTag.addChild();

            PowerConfiguration<?> typeConfig = power.getType().getConfig();
            PowerReference powerReference = PowerReference.of(power.getId());

            Power.DataEntry.CODEC.codec().encodeStart(NbtOps.INSTANCE, new Power.DataEntry(typeConfig, powerReference, powerType.toTag(), powerSources.get(power)))
                .mapError(err -> "Error encoding power \"" + power.getId() + "\" to NBT of entity " + owner.getName().getString() + " (UUID: " + owner.getStringUUID() + ") (skipping): " + err)
                .resultOrPartial(Apoli.LOGGER::warn)
                .ifPresent((tag) -> setWriteValueInternalTag(tag,powerTag));

        });

    }

    private static void setWriteValueInternalTag(Tag tag, ValueOutput powerTag) {
        ((TagValueOutput) powerTag).output = tag;
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeVarInt(0);
        PowerHolderComponent.super.writeSyncPacket(buf, recipient);
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {

        int syncType = buf.readVarInt();
        switch (syncType) {
            case 0 ->
                PowerHolderComponent.super.applySyncPacket(buf);
            case 1 ->
                PacketHandlers.GRANT_POWERS.apply(buf, this);
            case 2 ->
                PacketHandlers.REVOKE_POWERS.apply(buf, this);
            case 3 ->
                PacketHandlers.REVOKE_ALL_POWERS.apply(buf, this);
            default ->
                Apoli.LOGGER.warn("Received unknown sync type with ID {} (expected value range: [0 to 3]) when applying sync packet to entity {}! Skipping...", syncType, owner.getName().getString());
        }

    }

    @Override
    public void sync() {
        KEY.sync(this.owner);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("PowerHolderComponent[\n");
        for (Map.Entry<Power, PowerType> powerEntry : powers.entrySet()) {
            str.append("\t").append(powerEntry.getKey().getId()).append(": ").append(powerEntry.getValue().toTag().toString()).append("\n");
        }
        str.append("]");
        return str.toString();
    }

    private static PowerType shallowCopy(PowerType powerType) {

		//noinspection unchecked
		PowerConfiguration<PowerType> config = (PowerConfiguration<PowerType>) powerType.getConfig();
        TypedDataObjectFactory<PowerType> dataFactory = config.dataFactory();

        SerializableData.Instance data = dataFactory.toData(powerType);
        return dataFactory.fromData(data);

    }

}
