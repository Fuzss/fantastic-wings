package fuzs.fantasticwings.client.handler;

import fuzs.fantasticwings.client.FantasticWingsClient;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PotionItemModelHandler {
    public static final Item[] POTION_ITEMS = {
            Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION
    };
    private static final Set<ResourceLocation> POTION_MODEL_LOCATIONS = Stream.of(POTION_ITEMS)
            .map(item -> new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(item), "inventory"))
            .collect(Collectors.toSet());

    public static EventResultHolder<UnbakedModel> onModifyUnbakedModel(ResourceLocation modelLocation, Supplier<UnbakedModel> unbakedModel, Function<ResourceLocation, UnbakedModel> modelGetter, BiConsumer<ResourceLocation, UnbakedModel> modelAdder) {
        // we modify the potion item models to add our overrides
        // this is done in code instead of via overriding the vanilla json model to allow for better compatibility with resource packs and other mods wishing to do the same thing
        if (POTION_MODEL_LOCATIONS.contains(modelLocation)) {
            if (unbakedModel.get() instanceof BlockModel blockModel) {
                FlightApparatusImpl.forEach(flightApparatus -> {
                    registerItemOverride(blockModel,
                            flightApparatus.textureLocation(),
                            flightApparatus.modelLocation()
                    );
                });
                registerItemOverride(blockModel,
                        FantasticWingsClient.BAT_BLOOD_BOTTLE_TEXTURE_LOCATION,
                        AbstractModelProvider.decorateItemModelLocation(FantasticWingsClient.BAT_BLOOD_BOTTLE_TEXTURE_LOCATION)
                );

                return EventResultHolder.interrupt(blockModel);
            }
        }

        return EventResultHolder.pass();
    }

    private static void registerItemOverride(BlockModel blockModel, ResourceLocation itemModelProperty, ResourceLocation overrideModelLocation) {
        ItemOverride.Predicate itemPredicate = new ItemOverride.Predicate(itemModelProperty, 1.0F);
        ItemOverride itemOverride = new ItemOverride(overrideModelLocation, Collections.singletonList(itemPredicate));
        blockModel.getOverrides().add(itemOverride);
    }
}
