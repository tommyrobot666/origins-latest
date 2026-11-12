package io.github.apace100.apoli.action.type;

import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.action.context.ItemActionContext;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public abstract class ItemActionType extends ActionType<ItemActionContext, ItemAction> {

	@Override
	public ItemAction createAction() {
		return new ItemAction(this);
	}

	@Override
	public boolean shouldExecute(ItemActionContext context) {
		return !Objects.equals(context.stackReference(), SlotAccess.of(() -> ItemStack.EMPTY, (i) -> {
        }));
	}

}
