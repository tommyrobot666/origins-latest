package io.github.apace100.calio.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import org.jetbrains.annotations.Nullable;

public abstract class IdSimplePreparableReloadListener<T> extends SimplePreparableReloadListener<T> {
    public abstract Identifier getId();
    public abstract @Nullable Identifier getBefore();
    public abstract void giveLookup(HolderLookup.Provider wrapperLookup);
}
