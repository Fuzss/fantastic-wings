package fuzs.fantasticwings.common.init;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class FlightApparatuses {
    public static final ResourceKey<FlightApparatus> ANGEL_FLIGHT_APPARATUS = register("angel");
    public static final ResourceKey<FlightApparatus> PARROT_FLIGHT_APPARATUS = register("parrot");
    public static final ResourceKey<FlightApparatus> SLIME_FLIGHT_APPARATUS = register("slime");
    public static final ResourceKey<FlightApparatus> BLUE_BUTTERFLY_FLIGHT_APPARATUS = register("blue_butterfly");
    public static final ResourceKey<FlightApparatus> MONARCH_BUTTERFLY_FLIGHT_APPARATUS = register("monarch_butterfly");
    public static final ResourceKey<FlightApparatus> FIRE_FLIGHT_APPARATUS = register("fire");
    public static final ResourceKey<FlightApparatus> BAT_FLIGHT_APPARATUS = register("bat");
    public static final ResourceKey<FlightApparatus> FAIRY_FLIGHT_APPARATUS = register("fairy");
    public static final ResourceKey<FlightApparatus> EVIL_FLIGHT_APPARATUS = register("evil");
    public static final ResourceKey<FlightApparatus> DRAGON_FLIGHT_APPARATUS = register("dragon");
    public static final ResourceKey<FlightApparatus> METALLIC_FLIGHT_APPARATUS = register("metallic");

    private static ResourceKey<FlightApparatus> register(String name) {
        return ResourceKey.create(FlightApparatus.REGISTRY_KEY, FantasticWings.id(name));
    }

    public static void bootstrap(BootstrapContext<FlightApparatus> context) {
        registerFlightApparatus(context, ANGEL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, PARROT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, SLIME_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, BLUE_BUTTERFLY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, MONARCH_BUTTERFLY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, FIRE_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, BAT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, FAIRY_FLIGHT_APPARATUS, FlightApparatus.Model.INSECTOID);
        registerFlightApparatus(context, EVIL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, DRAGON_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
        registerFlightApparatus(context, METALLIC_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN);
    }

    private static void registerFlightApparatus(BootstrapContext<FlightApparatus> context, ResourceKey<FlightApparatus> key, FlightApparatus.Model model) {
        context.register(key, new FlightApparatus(key.identifier(), model));
    }
}
