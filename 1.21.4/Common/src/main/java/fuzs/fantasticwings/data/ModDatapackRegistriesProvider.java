package fuzs.fantasticwings.data;

import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.flight.apparatus.WingSettings;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class ModDatapackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDatapackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer consumer) {
        consumer.add(FlightApparatus.REGISTRY_KEY, ModDatapackRegistriesProvider::bootstrapFlightApparatuses);
    }

    static void bootstrapFlightApparatuses(BootstrapContext<FlightApparatus> context) {
        registerFlightApparatus(context,
                ModRegistry.ANGEL_FLIGHT_APPARATUS,
                FlightApparatus.Model.AVIAN,
                Items.FEATHER);
        registerFlightApparatus(context,
                ModRegistry.PARROT_FLIGHT_APPARATUS,
                FlightApparatus.Model.AVIAN,
                Items.RED_DYE);
        registerFlightApparatus(context,
                ModRegistry.SLIME_FLIGHT_APPARATUS,
                FlightApparatus.Model.INSECTOID,
                Items.SLIME_BALL);
        registerFlightApparatus(context,
                ModRegistry.BLUE_BUTTERFLY_FLIGHT_APPARATUS,
                FlightApparatus.Model.INSECTOID,
                Items.BLUE_DYE);
        registerFlightApparatus(context,
                ModRegistry.MONARCH_BUTTERFLY_FLIGHT_APPARATUS,
                FlightApparatus.Model.INSECTOID,
                Items.ORANGE_DYE);
        registerFlightApparatus(context,
                ModRegistry.FIRE_FLIGHT_APPARATUS,
                FlightApparatus.Model.AVIAN,
                Items.BLAZE_POWDER);
        registerFlightApparatus(context, ModRegistry.BAT_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN, Items.LEATHER);
        registerFlightApparatus(context,
                ModRegistry.FAIRY_FLIGHT_APPARATUS,
                FlightApparatus.Model.INSECTOID,
                Items.OXEYE_DAISY);
        registerFlightApparatus(context, ModRegistry.EVIL_FLIGHT_APPARATUS, FlightApparatus.Model.AVIAN, Items.BONE);
        registerFlightApparatus(context,
                ModRegistry.DRAGON_FLIGHT_APPARATUS,
                FlightApparatus.Model.AVIAN,
                Items.FIRE_CHARGE);
        registerFlightApparatus(context,
                ModRegistry.METALLIC_FLIGHT_APPARATUS,
                FlightApparatus.Model.AVIAN,
                Items.IRON_INGOT);
    }

    static void registerFlightApparatus(BootstrapContext<FlightApparatus> context, ResourceKey<FlightApparatus> resourceKey, FlightApparatus.Model model, Item item) {
        context.register(resourceKey,
                new FlightApparatus(resourceKey.location(),
                        model,
                        Optional.of(resourceKey.location()),
                        HolderSet.direct(item.builtInRegistryHolder()),
                        new WingSettings(6, 0.0001F, 2, 0.005F)));
    }
}
