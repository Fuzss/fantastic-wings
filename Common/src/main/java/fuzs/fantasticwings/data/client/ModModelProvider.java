package fuzs.fantasticwings.data.client;

import fuzs.fantasticwings.client.renderer.item.properties.select.FlightApparatusProperty;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.models.ItemModelGenerationHelper;
import fuzs.puzzleslib.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.ResourceKey;
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

        List<SelectItemModel.SwitchCase<ResourceKey<FlightApparatus>>> list = Stream.of(ModRegistry.ANGEL_FLIGHT_APPARATUS)
                .map((ResourceKey<FlightApparatus> resourceKey) -> {
                    ItemModel.Unbaked itemModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(
                            ModRegistry.BOTTLED_WINGS_ITEM.value(),
                            "_" + resourceKey.location().getPath(),
                            ModelTemplates.FLAT_ITEM));
                    return ItemModelUtils.when(resourceKey, itemModel);
                })
                .toList();

        ItemModel.Unbaked itemModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(ModRegistry.BOTTLED_WINGS_ITEM.value(),
                Items.GLASS_BOTTLE,
                ModelTemplates.FLAT_ITEM));
        itemModelGenerators.itemModelOutput.accept(ModRegistry.BOTTLED_WINGS_ITEM.value(), ItemModelUtils.select(new FlightApparatusProperty(), itemModel, list));

        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.ANGEL_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.PARROT_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.SLIME_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.BLUE_BUTTERFLY_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.MONARCH_BUTTERFLY_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.FIRE_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.BAT_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.FAIRY_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.EVIL_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.DRAGON_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
        ItemModelGenerationHelper.createFlatItemModel(ModelLocationHelper.getItemModel(ModRegistry.METALLIC_FLIGHT_APPARATUS.location()),
                ModelTemplates.FLAT_ITEM,
                itemModelGenerators.modelOutput);
    }
}
