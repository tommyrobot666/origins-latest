package io.github.apace100.apoli.data.container;

import io.github.apace100.apoli.util.TextAlignment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.Range;

public interface ContainerType {

	TextAlignment titleAlignment();

	MenuConstructor create(Inventory inventory);

	default int size() {
		return columns() * rows();
	}

	@Range(from = 1, to = Integer.MAX_VALUE)
	int columns();

	@Range(from = 1, to = Integer.MAX_VALUE)
	int rows();

}
