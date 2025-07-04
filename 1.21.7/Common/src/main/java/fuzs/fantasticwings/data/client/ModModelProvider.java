package fuzs.fantasticwings.data.client;

import fuzs.fantasticwings.client.renderer.item.properties.select.FlightApparatusProperty;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.FlightApparatuses;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.stream.Stream;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModRegistry.BOTTLED_BAT_BLOOD_ITEM.value(), ModelTemplates.FLAT_ITEM);
        this.generateBottledWings(ModRegistry.BOTTLED_WINGS_ITEM.value(), itemModelGenerators);
    }

    public final void generateBottledWings(Item item, ItemModelGenerators itemModelGenerators) {
        List<SelectItemModel.SwitchCase<ResourceKey<FlightApparatus>>> switchCases = Stream.of(FlightApparatuses.ANGEL_FLIGHT_APPARATUS,
                FlightApparatuses.PARROT_FLIGHT_APPARATUS,
                FlightApparatuses.SLIME_FLIGHT_APPARATUS,
                FlightApparatuses.BLUE_BUTTERFLY_FLIGHT_APPARATUS,
                FlightApparatuses.MONARCH_BUTTERFLY_FLIGHT_APPARATUS,
                FlightApparatuses.FIRE_FLIGHT_APPARATUS,
                FlightApparatuses.BAT_FLIGHT_APPARATUS,
                FlightApparatuses.EVIL_FLIGHT_APPARATUS,
                FlightApparatuses.FAIRY_FLIGHT_APPARATUS,
                FlightApparatuses.DRAGON_FLIGHT_APPARATUS,
                FlightApparatuses.METALLIC_FLIGHT_APPARATUS).map((ResourceKey<FlightApparatus> resourceKey) -> {
            return ItemModelUtils.when(resourceKey,
                    ItemModelUtils.plainModel(this.createWings(item, resourceKey, itemModelGenerators)));
        }).toList();
        ItemModel.Unbaked itemModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item,
                Items.GLASS_BOTTLE,
                ModelTemplates.FLAT_ITEM));
        itemModelGenerators.itemModelOutput.accept(item,
                ItemModelUtils.select(new FlightApparatusProperty(), itemModel, switchCases));
    }

    public final ResourceLocation createWings(Item item, ResourceKey<FlightApparatus> resourceKey, ItemModelGenerators itemModelGenerators) {
        return itemModelGenerators.createFlatItemModel(item,
                "_" + resourceKey.location().getPath(),
                ModelTemplates.FLAT_ITEM);
    }
}
