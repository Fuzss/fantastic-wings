package fuzs.fantasticwings.client.flight.apparatus;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.animator.AnimatorAvian;
import fuzs.fantasticwings.client.animator.AnimatorInsectoid;
import fuzs.fantasticwings.client.model.AvianWingsModel;
import fuzs.fantasticwings.client.model.InsectoidWingsModel;
import fuzs.fantasticwings.client.model.WingsModel;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class WingFormRegistry implements ResourceManagerReloadListener {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(FantasticWings.MOD_ID);
    public static final ModelLayerLocation AVIAN_WINGS_MODEL_LAYER = MODEL_LAYERS.registerModelLayer("avian_wings");
    public static final ModelLayerLocation INSECTOID_WINGS_MODEL_LAYER = MODEL_LAYERS.registerModelLayer(
            "insectoid_wings");
    public static final WingFormRegistry INSTANCE = new WingFormRegistry();

    private WingsModel<AnimatorAvian> avianWings;
    private WingsModel<AnimatorInsectoid> insectoidWings;

    private WingFormRegistry() {
        // NO-OP
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
        this.avianWings = new AvianWingsModel(entityModels.bakeLayer(AVIAN_WINGS_MODEL_LAYER));
        this.insectoidWings = new InsectoidWingsModel(entityModels.bakeLayer(INSECTOID_WINGS_MODEL_LAYER));
    }

    public WingForm<?> createWings(Holder<FlightApparatus> holder) {
        ResourceLocation resourceLocation = FlightApparatus.transformTextureLocation(FlightApparatus.getTextureLocation(
                holder.unwrapKey().orElseThrow()));
        return switch (holder.value().model()) {
            case AVIAN -> this.createAvianWings(resourceLocation);
            case INSECTOID -> this.createInsectoidWings(resourceLocation);
        };
    }

    private WingForm<AnimatorAvian> createAvianWings(ResourceLocation resourceLocation) {
        return WingForm.of(AnimatorAvian::new, () -> this.avianWings, resourceLocation);
    }

    private WingForm<AnimatorInsectoid> createInsectoidWings(ResourceLocation resourceLocation) {
        return WingForm.of(AnimatorInsectoid::new, () -> this.insectoidWings, resourceLocation);
    }
}
