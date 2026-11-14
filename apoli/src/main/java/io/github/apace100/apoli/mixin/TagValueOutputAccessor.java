package io.github.apace100.apoli.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.TagValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagValueOutput.class)
public interface TagValueOutputAccessor {
    @Accessor("output")
    void output(CompoundTag t);
}
