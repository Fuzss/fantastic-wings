package fuzs.fantasticwings.client;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.flight.FlightView;
import fuzs.fantasticwings.client.flight.apparatus.WingFormRegistry;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.client.model.AvianWingsModel;
import fuzs.fantasticwings.client.model.InsectoidWingsModel;
import fuzs.fantasticwings.client.renderer.entity.layers.ModWingsLayer;
import fuzs.fantasticwings.client.renderer.item.properties.select.FlightApparatusProperty;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.fantasticwings.world.item.WithDescriptionItem;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LivingEntityRenderLayersContext;
import fuzs.puzzleslib.api.client.event.v1.AddResourcePackReloadListenersCallback;
import fuzs.puzzleslib.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ComputeCameraAnglesCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderHandEvents;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

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
        ExtractRenderStateCallback.EVENT.register(ClientEventHandler::onExtractRenderState);
        RenderHandEvents.OFF_HAND.register(ClientEventHandler::onRenderOffHand);
        AddResourcePackReloadListenersCallback.EVENT.register((BiConsumer<ResourceLocation, PreparableReloadListener> consumer) -> {
            consumer.accept(FantasticWings.id("wing_models"), WingFormRegistry.INSTANCE);
        });
    }

    @Override
    public void onClientSetup() {
        ItemTooltipRegistry.registerItemTooltip(WithDescriptionItem.class,
                WithDescriptionItem::getDescriptionComponent);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(ClientModRegistry.FLY_KEY_MAPPING,
                KeyActivationHandler.forGame((Minecraft minecraft) -> {
                    Player player = minecraft.player;
                    FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(player);
                    if (flightCapability.canFly(player)) {
                        flightCapability.toggleIsFlying(player, PlayerSet.ofNone());
                        MessageSender.broadcast(new ServerboundControlFlyingMessage(flightCapability.isFlying()));
                    }
                }));
    }

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        context.registerSelectItemModelProperty(FantasticWings.id("wings"), FlightApparatusProperty.TYPE);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(WingFormRegistry.INSECTOID_WINGS_MODEL_LAYER,
                InsectoidWingsModel::createWingsLayer);
        context.registerLayerDefinition(WingFormRegistry.AVIAN_WINGS_MODEL_LAYER, AvianWingsModel::createWingsLayer);
    }

    @Override
    public void onRegisterLivingEntityRenderLayers(LivingEntityRenderLayersContext context) {
        context.registerRenderLayer(EntityType.PLAYER, ModWingsLayer::new);
    }
}
