package fuzs.fantasticwings.data.client;

import fuzs.fantasticwings.client.FantasticWingsClient;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        generateFlatItem(FantasticWingsClient.BAT_BLOOD_BOTTLE_TEXTURE_LOCATION, ModelTemplates.FLAT_ITEM, builder.output);
        FlightApparatusImpl.forEach(flightApparatus -> {
            generateFlatItem(flightApparatus.textureLocation(), ModelTemplates.FLAT_ITEM, builder.output);
        });
    }
}
