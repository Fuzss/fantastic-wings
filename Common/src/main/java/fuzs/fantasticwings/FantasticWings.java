package fuzs.fantasticwings;

import fuzs.fantasticwings.commands.WingsCommand;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.handler.ServerEventHandler;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.context.DataPackRegistriesContext;
import fuzs.puzzleslib.common.api.core.v1.context.PayloadTypesContext;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.common.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.common.api.event.v1.server.RegisterCommandsCallback;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FantasticWings implements ModConstructor {
    public static final String MOD_ID = "fantasticwings";
    public static final String MOD_NAME = "Fantastic Wings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

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
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToServer(ServerboundControlFlyingMessage.class, ServerboundControlFlyingMessage.STREAM_CODEC);
    }

    @Override
    public void onRegisterDataPackRegistries(DataPackRegistriesContext context) {
        context.registerSyncedRegistry(FlightApparatus.REGISTRY_KEY, FlightApparatus.DIRECT_CODEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
