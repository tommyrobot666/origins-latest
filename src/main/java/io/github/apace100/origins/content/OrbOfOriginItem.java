package io.github.apace100.origins.content;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.component.item.ItemOriginsComponent;
import io.github.apace100.origins.registry.ModDataComponentTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class OrbOfOriginItem extends Item {

    public OrbOfOriginItem() {
        this(new Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)
            .component(ModDataComponentTypes.ORIGIN, ItemOriginsComponent.DEFAULT)
                .setId(ResourceKey.create(Registries.ITEM, Origins.identifier("orb_of_origin"))));
    }

    public OrbOfOriginItem(Properties settings) {
        super(settings);
    }

}
