package fuzs.fantasticwings.data;

import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.flight.apparatus.WingSettings;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class ModDatapackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDatapackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer consumer) {
        consumer.add(FlightApparatus.REGISTRY_KEY, ModDatapackRegistriesProvider::bootstrapFlightApparatuses);
    }

    static void bootstrapFlightApparatuses(BootstrapContext<FlightApparatus> context) {
        registerFlightApparatus(context, ModRegistry.ANGEL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, ModRegistry.PARROT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, ModRegistry.SLIME_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, ModRegistry.BLUE_BUTTERFLY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context,
                ModRegistry.MONARCH_BUTTERFLY_FLIGHT_APPARATUS,
                FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, ModRegistry.FIRE_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, ModRegistry.BAT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, ModRegistry.FAIRY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, ModRegistry.EVIL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, ModRegistry.DRAGON_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, ModRegistry.METALLIC_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
    }

    static void registerFlightApparatus(BootstrapContext<FlightApparatus> context, ResourceKey<FlightApparatus> resourceKey, FlightApparatus.Model model) {
        context.register(resourceKey,
                new FlightApparatus(resourceKey.location(), model, new WingSettings(6, 0.0001F, 2, 0.005F)));
    }
}
