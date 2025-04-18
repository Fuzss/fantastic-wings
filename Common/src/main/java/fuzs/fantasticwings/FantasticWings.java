package fuzs.fantasticwings;

import fuzs.fantasticwings.commands.WingsCommand;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.handler.ServerEventHandler;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.DataPackRegistriesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.event.v1.server.RegisterCommandsCallback;
import fuzs.puzzleslib.api.network.v3.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FantasticWings implements ModConstructor {
    public static final String MOD_ID = "fantasticwings";
    public static final String MOD_NAME = "Fantastic Wings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.builder(MOD_ID)
            .registerServerbound(ServerboundControlFlyingMessage.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        RegisterCommandsCallback.EVENT.register(WingsCommand::register);
        EntityRidingEvents.START.register(ServerEventHandler::onStartRiding);
        PlayerTickEvents.END.register(ServerEventHandler::onEndPlayerTick);
        PlayerInteractEvents.ATTACK_ENTITY.register(ServerEventHandler::onAttackEntity);
    }

    @Override
    public void onDataPackRegistriesContext(DataPackRegistriesContext context) {
        context.registerSyncedRegistry(FlightApparatus.REGISTRY_KEY, FlightApparatus.DIRECT_CODEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
