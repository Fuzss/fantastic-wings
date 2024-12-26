package fuzs.fantasticwings;

import fuzs.fantasticwings.commands.WingsCommand;
import fuzs.fantasticwings.config.ServerConfig;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.fantasticwings.handler.ServerEventHandler;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.event.v1.server.RegisterCommandsCallback;
import fuzs.puzzleslib.api.event.v1.server.RegisterPotionBrewingMixesCallback;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import fuzs.puzzleslib.api.network.v3.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class FantasticWings implements ModConstructor {
    public static final String MOD_ID = "fantasticwings";
    public static final String MOD_NAME = "Fantastic Wings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.builder(MOD_ID)
            .registerServerbound(ServerboundControlFlyingMessage.class);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        RegisterCommandsCallback.EVENT.register(WingsCommand::register);
        EntityRidingEvents.START.register(ServerEventHandler::onStartRiding);
        PlayerTickEvents.END.register(ServerEventHandler::onEndPlayerTick);
        PlayerInteractEvents.ATTACK_ENTITY.register(ServerEventHandler::onAttackEntity);
        RegisterPotionBrewingMixesCallback.EVENT.register((RegisterPotionBrewingMixesCallback.Builder builder) -> {
            FlightApparatusImpl.forEach(flightApparatus -> flightApparatus.onRegisterPotionBrewingMixes(builder));
        });
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID).icon(() -> {
            return PotionContents.createItemStack(Items.POTION,
                    FlightApparatusImpl.MONARCH_BUTTERFLY.getPotion()
            );
        }).icons(() -> {
            return Stream.of(FlightApparatusImpl.values()).map(flightApparatus -> {
                return PotionContents.createItemStack(Items.POTION, flightApparatus.getPotion());
            }).toArray(ItemStack[]::new);
        }).displayItems((itemDisplayParameters, output) -> {
            output.accept(PotionContents.createItemStack(Items.POTION,
                    ModRegistry.BAT_BLOOD_POTION
            ));
            FlightApparatusImpl.forEach(flightApparatus -> {
                output.accept(PotionContents.createItemStack(Items.POTION, flightApparatus.getPotion()));
            });
        }));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
