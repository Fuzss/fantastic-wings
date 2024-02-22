package fuzs.fantasticwings.client;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import fuzs.fantasticwings.client.init.ModClientCapabilities;
import fuzs.fantasticwings.client.init.WingFormRegistry;
import fuzs.fantasticwings.client.renderer.entity.layers.LayerWings;
import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.server.flight.FlightCapability;
import fuzs.fantasticwings.server.network.ServerboundControlFlyingMessage;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LivingEntityRenderLayersContext;
import fuzs.puzzleslib.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ComputeCameraAnglesCallback;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;

public class FantasticWingsClient implements ClientModConstructor {
    public static final KeyMapping FLY_KEY_MAPPING = KeyMappingHelper.registerKeyMapping(FantasticWings.id("fly"), InputConstants.KEY_R);

    @Override
    public void onConstructMod() {
        ModClientCapabilities.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        ComputeCameraAnglesCallback.EVENT.register(ClientEventHandler::onComputeCameraAngles);
        ClientEntityLevelEvents.LOAD.register(ClientEventHandler::onEntityLoad);
        PlayerTickEvents.END.register(ClientEventHandler::onEndPlayerTick);
    }

    @Override
    public void onClientSetup() {
        WingFormRegistry.INSTANCE.registerAll();
    }

    @Override
    public void onRegisterResourcePackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener("wing_models", WingFormRegistry.INSTANCE);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(FLY_KEY_MAPPING, KeyActivationHandler.forGame((Minecraft minecraft) -> {
            FlightCapability flightCapability = ModCapabilities.FLIGHT_CAPABILITY.get(minecraft.player);
            if (flightCapability.canFly()) {
                flightCapability.toggleIsFlying(PlayerSet.ofNone());
                FantasticWings.NETWORK.sendMessage(new ServerboundControlFlyingMessage(flightCapability.isFlying()));
            }
        }));
    }

    @Override
    public void onRegisterLivingEntityRenderLayers(LivingEntityRenderLayersContext context) {
        context.registerRenderLayer(EntityType.PLAYER, LayerWings::new);
    }
}
