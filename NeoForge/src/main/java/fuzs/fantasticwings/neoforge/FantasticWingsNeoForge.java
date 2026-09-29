package fuzs.fantasticwings.neoforge;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.fantasticwings.common.data.recipes.ModBrewingProvider;
import fuzs.fantasticwings.common.data.tags.ModEntityTypeTagsProvider;
import fuzs.fantasticwings.common.data.tags.ModItemTagsProvider;
import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.common.init.FlightApparatuses;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import net.neoforged.fml.common.Mod;

@Mod(FantasticWings.MOD_ID)
public class FantasticWingsNeoForge {

    public FantasticWingsNeoForge() {
        ModConstructor.construct(FantasticWings.MOD_ID, FantasticWings::new);
        DataProviderBuilder.of(FantasticWings.MOD_ID)
                .addWorldBootstrap(FlightApparatus.REGISTRY_KEY, FlightApparatuses::bootstrap)
                .addProvider(ModEntityTypeTagsProvider::new, ModItemTagsProvider::new)
                .addRecipeProvider(ModBrewingProvider::new);
    }
}
