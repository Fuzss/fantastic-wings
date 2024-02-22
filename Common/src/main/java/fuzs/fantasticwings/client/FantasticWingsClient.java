package fuzs.fantasticwings.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.sun.jna.platform.win32.OaIdl;
import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import fuzs.fantasticwings.client.handler.FlyingCrouchHandler;
import fuzs.fantasticwings.client.init.ModClientCapabilities;
import fuzs.fantasticwings.client.init.ModModelLayers;
import fuzs.fantasticwings.client.init.WingFormRegistry;
import fuzs.fantasticwings.client.model.ModelWingsInsectoid;
import fuzs.fantasticwings.client.renderer.entity.layers.LayerWings;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.network.ServerboundControlFlyingMessage;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.AdditionalModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LivingEntityRenderLayersContext;
import fuzs.puzzleslib.api.client.event.v1.ModelEvents;
import fuzs.puzzleslib.api.client.event.v1.entity.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ComputeCameraAnglesCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderHandCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderPlayerEvents;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FantasticWingsClient implements ClientModConstructor {
    public static final KeyMapping FLY_KEY_MAPPING = KeyMappingHelper.registerKeyMapping(FantasticWings.id("fly"),
            InputConstants.KEY_R
    );
    public static final ResourceLocation BAT_BLOOD_BOTTLE_TEXTURE_LOCATION = FantasticWings.id("bat_blood_bottle");

    @Override
    public void onConstructMod() {
        ModClientCapabilities.touch();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ComputeCameraAnglesCallback.EVENT.register(ClientEventHandler::onComputeCameraAngles);
        ClientEntityLevelEvents.LOAD.register(ClientEventHandler::onEntityLoad);
        PlayerTickEvents.END.register(ClientEventHandler::onEndPlayerTick);
        RenderHandCallback.EVENT.register(ClientEventHandler::onRenderHand);
        RenderPlayerEvents.BEFORE.register(FlyingCrouchHandler::onBeforeRenderPlayer);
        RenderPlayerEvents.AFTER.register(FlyingCrouchHandler::onAfterRenderPlayer);
        ModelEvents.MODIFY_BAKED_MODEL.register((ResourceLocation modelLocation, Supplier<BakedModel> bakedModel, Supplier<ModelBaker> modelBaker, Function<ResourceLocation, BakedModel> modelGetter, BiConsumer<ResourceLocation, BakedModel> modelAdder) -> {
            if (modelLocation.getPath().contains("potion")) {
                System.out.println();
            }
            return EventResultHolder.pass();
        });
        ModelEvents.MODIFY_UNBAKED_MODEL.register((ResourceLocation modelLocation, Supplier<UnbakedModel> unbakedModel, Function<ResourceLocation, UnbakedModel> modelGetter, BiConsumer<ResourceLocation, UnbakedModel> modelAdder) -> {
            if (modelLocation.getPath().contains("potion")) {
                System.out.println();
            }
            return EventResultHolder.pass();
        });
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
    public void onRegisterAdditionalModels(AdditionalModelsContext context) {
        context.registerAdditionalModel(BAT_BLOOD_BOTTLE_TEXTURE_LOCATION);
        FlightApparatusImpl.forEach(flightApparatus -> {
            context.registerAdditionalModel(flightApparatus.textureLocation());
        });
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModModelLayers.INSECTOID_WINGS, ModelWingsInsectoid::createWingsLayer);
    }

    @Override
    public void onRegisterLivingEntityRenderLayers(LivingEntityRenderLayersContext context) {
        context.registerRenderLayer(EntityType.PLAYER, LayerWings::new);
    }
}
