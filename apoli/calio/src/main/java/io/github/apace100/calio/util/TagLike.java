package io.github.apace100.calio.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.apace100.calio.codec.*;
import io.github.apace100.calio.data.DataException;
import io.github.apace100.calio.mixin.HolderSetBackedAccessor;
import io.github.apace100.calio.mixin.TagEntryAccessor;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unused")
public class TagLike<T> {

    private final ResourceKey<? extends Registry<T>> registryRef;
    private final ImmutableSet<TagEntry> tagEntries;

    private final ImmutableMap<ResourceKey<T>, T> elementsByKey;
    private final ImmutableMap<TagKey<T>, Collection<T>> elementsByTag;

    protected TagLike(ResourceKey<? extends Registry<T>> registryRef, Map<ResourceKey<T>, T> elementsByKey, Map<TagKey<T>, Collection<T>> elementsByTag, Collection<TagEntry> tagEntries) {
        this.registryRef = registryRef;
        this.elementsByKey = ImmutableMap.copyOf(elementsByKey);
        this.elementsByTag = ImmutableMap.copyOf(elementsByTag);
        this.tagEntries = ImmutableSet.copyOf(tagEntries);
    }

    public ImmutableSet<TagEntry> entries() {
        return tagEntries;
    }

    public boolean contains(@NotNull T element) {
        return elementsByKey.containsValue(element)
            || elementsByTag.values().stream().flatMap(Collection::stream).anyMatch(element::equals);
    }

    public void write(RegistryFriendlyByteBuf buf) {

        buf.writeResourceKey(registryRef);
        CalioPacketCodecs.TAG_ENTRY_SET.encode(buf, entries());

        buf.writeBoolean(RegistrySynchronization.isNetworkable(registryRef));

    }

    public static <T> TagLike<T> read(RegistryFriendlyByteBuf buf) {

        ResourceKey<? extends Registry<T>> registryRef = buf.readRegistryKey();
        Set<TagEntry> tagEntries = CalioPacketCodecs.TAG_ENTRY_SET.decode(buf);

        boolean syncedRegistry = buf.readBoolean();
        return syncedRegistry
            ? builder(registryRef).build(buf.registryAccess().getOrThrow(registryRef).value())
            : new TagLike<>(registryRef, Map.of(), Map.of(), tagEntries);

    }

    public static <T> Builder<T> builder(ResourceKey<? extends Registry<T>> registryRef) {
        return new Builder<>(registryRef);
    }

    public static <T> Builder<T> builder(ResourceKey<? extends Registry<T>> registryRef, Collection<TagEntry> tagEntries) {
        return new Builder<>(registryRef, tagEntries);
    }

    public static class Builder<E> {

        private final ResourceKey<? extends Registry<E>> registryRef;
        private final Set<TagEntry> tagEntries;

        public Builder(ResourceKey<? extends Registry<E>> registryRef) {
            this.registryRef = registryRef;
            this.tagEntries = new ObjectOpenHashSet<>();
        }

        public Builder(TagLike<E> tagLike) {
            this.registryRef = tagLike.registryRef;
            this.tagEntries = new ObjectOpenHashSet<>(tagLike.entries());
        }

        public Builder(ResourceKey<? extends Registry<E>> registryRef, Collection<TagEntry> tagEntries) {
            this.registryRef = registryRef;
            this.tagEntries = new ObjectOpenHashSet<>(tagEntries);
        }

        public Builder<E> add(TagEntry tagEntry) {
            this.tagEntries.add(tagEntry);
            return this;
        }

        public Builder<E> addAll(Collection<TagEntry> tagEntries) {
            this.tagEntries.addAll(tagEntries);
            return this;
        }

        public Builder<E> addAll(Builder<E> other) {
            return this.addAll(other.tagEntries);
        }

        public Builder<E> clear() {
            this.tagEntries.clear();
            return this;
        }

        @SuppressWarnings("unchecked")
        public TagLike<E> build(@NotNull HolderGetter<E> entryLookup) {

            Map<ResourceKey<E>, E> elementsByKey = new HashMap<>();
            Map<TagKey<E>, Collection<E>> elementsByTag = new HashMap<>();

            Set<TagEntry> tagEntries = new HashSet<>();
            for (TagEntry tagEntry : this.tagEntries) {

                TagEntryAccessor tagEntryAccess = (TagEntryAccessor) tagEntry;

                Identifier id = tagEntryAccess.getId();
                int index = tagEntries.size();

                boolean isTag = tagEntryAccess.isTag();
                boolean required = tagEntryAccess.isRequired();

                if (isTag) {

                    TagKey<E> tag = TagKey.create(registryRef, id);
                    Optional<HolderSet.Named<E>> entries = entryLookup.get(tag);

                    if (required && entries.isEmpty()) {
                        throw new DataException(DataException.Phase.READING, index, "Tag \"" + id + "\" for registry \"" + registryRef.identifier() + "\" doesn't exist!");
                    }

                    else if (entries.isPresent()) {
                        Collection<E> elements = elementsByTag.computeIfAbsent(tag, k -> new ObjectOpenHashSet<>());
                        entries.stream()
                            .map(registryEntries -> ((HolderSetBackedAccessor<E>) registryEntries).callContents())
                            .flatMap(Collection::stream)
                            .map(Holder::value)
                            .forEach(elements::add);
                    }

                }

                else {

                    ResourceKey<E> key = ResourceKey.create(registryRef, id);
                    Optional<Holder.Reference<E>> entry = entryLookup.get(key);

                    if (required && entry.isEmpty()) {
                        throw new DataException(DataException.Phase.READING, index, "Type \"" + id + "\" is not registered in registry \"" + registryRef.identifier() + "\"!");
                    }

                    else {
                        entry.ifPresent(reference -> elementsByKey.put(key, reference.value()));
                    }

                }

                tagEntries.add(tagEntry);

            }

            return new TagLike<>(registryRef, elementsByKey, elementsByTag, tagEntries);

        }

    }

}
