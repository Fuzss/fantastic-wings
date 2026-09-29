package fuzs.fantasticwings.common.init;

import fuzs.fantasticwings.common.world.item.BottledWingsItem;
import fuzs.fantasticwings.common.world.item.WithDescriptionItem;
import fuzs.fantasticwings.common.world.item.consume_effects.TakeWingsConsumeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;

public class ModItems {
    public static final Holder.Reference<Item> BOTTLED_WINGS_ITEM = ModRegistry.REGISTRIES.registerItem("bottled_wings",
            BottledWingsItem::new,
            ModItems::bottledItemProperties);
    public static final Holder.Reference<Item> BOTTLED_BAT_BLOOD_ITEM = ModRegistry.REGISTRIES.registerItem(
            "bottled_bat_blood",
            WithDescriptionItem::new,
            () -> bottledItemProperties().component(DataComponents.CONSUMABLE,
                    Consumables.defaultDrink().onConsume(new TakeWingsConsumeEffect()).build()));

    public static void bootstrap() {
        // NO-OP
    }

    private static Item.Properties bottledItemProperties() {
        return new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE);
    }
}
