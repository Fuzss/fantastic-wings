package fuzs.fantasticwings.client;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.flight.FlightView;
import fuzs.fantasticwings.client.flight.apparatus.WingFormRegistry;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.client.model.AvianWingsModel;
import fuzs.fantasticwings.client.model.InsectoidWingsModel;
import fuzs.fantasticwings.client.model.geom.ModModelLayers;
import fuzs.fantasticwings.client.renderer.entity.layers.ModWingsLayer;
import fuzs.fantasticwings.client.renderer.item.properties.select.FlightApparatusProperty;
import fuzs.fantasticwings.flight.Flight;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.fantasticwings.world.item.WithDescriptionItem;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.core.v1.context.ItemModelsContext;
import fuzs.puzzleslib.common.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.common.api.client.core.v1.context.ResourcePackReloadListenersContext;
import fuzs.puzzleslib.common.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.common.api.client.event.v1.renderer.AddLivingEntityRenderLayersCallback;
import fuzs.puzzleslib.common.api.client.event.v1.renderer.ComputeCameraAnglesCallback;
import fuzs.puzzleslib.common.api.client.event.v1.renderer.ExtractEntityRenderStateCallback;
import fuzs.puzzleslib.common.api.client.event.v1.renderer.RenderHandEvents;
import fuzs.puzzleslib.common.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import fuzs.puzzleslib.common.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.common.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.common.api.network.v4.MessageSender;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class FantasticWingsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        ClientModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ComputeCameraAnglesCallback.EVENT.register(ClientEventHandler::onComputeCameraAngles);
        ClientEntityLevelEvents.LOAD.register(ClientEventHandler::onEntityLoad);
        PlayerTickEvents.END.register(FlightView::onEndPlayerTick);
        ExtractEntityRenderStateCallback.EVENT.register(ClientEventHandler::onExtractEntityRenderState);
        RenderHandEvents.OFF_HAND.register(ClientEventHandler::onRenderOffHand);
        AddLivingEntityRenderLayersCallback.EVENT.register(ModWingsLayer::addLivingEntityRenderLayers);
    }

    @Override
    public void onClientSetup() {
        ItemTooltipRegistry.ITEM.registerItemTooltip(WithDescriptionItem.class,
                WithDescriptionItem::getDescriptionComponent);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(ClientModRegistry.FLY_KEY_MAPPING,
                KeyActivationHandler.forGame((Minecraft minecraft) -> {
                    Player player = minecraft.player;
                    Flight flight = ModRegistry.FLIGHT_ATTACHMENT_TYPE.get(player);
                    if (flight.canFly(player)) {
                        flight = flight.toggleIsFlying(player);
                        ModRegistry.FLIGHT_ATTACHMENT_TYPE.set(player, flight);
                        MessageSender.broadcast(new ServerboundControlFlyingMessage(flight.isFlying()));
                    }
                }));
    }

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        context.registerSelectItemModelProperty(FantasticWings.id("wings"), FlightApparatusProperty.TYPE);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModModelLayers.INSECTOID_WINGS_MODEL_LAYER,
                InsectoidWingsModel::createWingsLayer);
        context.registerLayerDefinition(ModModelLayers.AVIAN_WINGS_MODEL_LAYER, AvianWingsModel::createWingsLayer);
    }

    @Override
    public void onAddResourcePackReloadListeners(ResourcePackReloadListenersContext context) {
        context.registerReloadListener(FantasticWings.id("wing_models"), WingFormRegistry.INSTANCE);
    }
}
