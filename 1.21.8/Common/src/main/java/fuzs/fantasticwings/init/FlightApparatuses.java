package fuzs.fantasticwings.init;

import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.flight.apparatus.WingSettings;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class FlightApparatuses {
    public static final ResourceKey<FlightApparatus> ANGEL_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "angel");
    public static final ResourceKey<FlightApparatus> PARROT_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "parrot");
    public static final ResourceKey<FlightApparatus> SLIME_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "slime");
    public static final ResourceKey<FlightApparatus> BLUE_BUTTERFLY_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "blue_butterfly");
    public static final ResourceKey<FlightApparatus> MONARCH_BUTTERFLY_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "monarch_butterfly");
    public static final ResourceKey<FlightApparatus> FIRE_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "fire");
    public static final ResourceKey<FlightApparatus> BAT_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "bat");
    public static final ResourceKey<FlightApparatus> FAIRY_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "fairy");
    public static final ResourceKey<FlightApparatus> EVIL_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(FlightApparatus.REGISTRY_KEY,
            "evil");
    public static final ResourceKey<FlightApparatus> DRAGON_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "dragon");
    public static final ResourceKey<FlightApparatus> METALLIC_FLIGHT_APPARATUS = ModRegistry.REGISTRIES.makeResourceKey(
            FlightApparatus.REGISTRY_KEY,
            "metallic");

    public static void bootstrap(BootstrapContext<FlightApparatus> context) {
        registerFlightApparatus(context, ANGEL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, PARROT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, SLIME_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, BLUE_BUTTERFLY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, MONARCH_BUTTERFLY_FLIGHT_APPARATUS,
                FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, FIRE_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, BAT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, FAIRY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, EVIL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, DRAGON_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, METALLIC_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
    }

    static void registerFlightApparatus(BootstrapContext<FlightApparatus> context, ResourceKey<FlightApparatus> resourceKey, FlightApparatus.Model model) {
        context.register(resourceKey,
                new FlightApparatus(resourceKey.location(), model, new WingSettings(6, 0.0001F, 2, 0.005F)));
    }
}
