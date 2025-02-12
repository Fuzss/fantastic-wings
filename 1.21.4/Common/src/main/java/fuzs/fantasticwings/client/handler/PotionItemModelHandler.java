package fuzs.fantasticwings.client.handler;

import com.google.common.collect.ImmutableSet;
import fuzs.fantasticwings.client.FantasticWingsClient;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.resources.model.ItemModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PotionItemModelHandler {
    public static final Item[] POTION_ITEMS = {
            Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION
    };
    private static final Set<ModelResourceLocation> POTION_MODEL_LOCATIONS = Stream.of(POTION_ITEMS)
            .map(item -> ModelResourceLocation.inventory(item.builtInRegistryHolder().key().location()))
            .collect(ImmutableSet.toImmutableSet());

    public static EventResultHolder<UnbakedModel> onModifyUnbakedModel(ModelResourceLocation modelLocation, Supplier<UnbakedModel> unbakedModel, Function<ModelResourceLocation, UnbakedModel> modelGetter, BiConsumer<ResourceLocation, UnbakedModel> modelAdder) {
        // we modify the potion item models to add our overrides
        // this is done in code instead of via overriding the vanilla json model
        // to allow for better compatibility with resource packs and other mods wishing to do the same thing
        if (POTION_MODEL_LOCATIONS.contains(modelLocation) && unbakedModel.get() instanceof ItemModel itemModel) {
            FlightApparatusImpl.forEach(flightApparatus -> {
                registerItemOverride(itemModel, flightApparatus.textureLocation(), flightApparatus.modelLocation());
            });
            registerItemOverride(itemModel,
                    FantasticWingsClient.BAT_BLOOD_BOTTLE_TEXTURE_LOCATION,
                    AbstractModelProvider.decorateItemModelLocation(FantasticWingsClient.BAT_BLOOD_BOTTLE_TEXTURE_LOCATION));
            return EventResultHolder.interrupt(itemModel);
        } else {
            return EventResultHolder.pass();
        }
    }

    private static void registerItemOverride(ItemModel itemModel, ResourceLocation itemModelProperty, ResourceLocation overrideModelLocation) {
        ItemOverride.Predicate itemPredicate = new ItemOverride.Predicate(itemModelProperty, 1.0F);
        ItemOverride itemOverride = new ItemOverride(overrideModelLocation, Collections.singletonList(itemPredicate));
        if (!(itemModel.overrides instanceof ArrayList<ItemOverride>)) {
            itemModel.overrides = new ArrayList<>(itemModel.overrides);
        }
        itemModel.overrides.add(itemOverride);
    }
}
