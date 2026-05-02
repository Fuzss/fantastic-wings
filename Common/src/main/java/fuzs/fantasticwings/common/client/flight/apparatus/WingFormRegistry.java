package fuzs.fantasticwings.common.client.flight.apparatus;

import fuzs.fantasticwings.common.client.animator.AnimatorAvian;
import fuzs.fantasticwings.common.client.animator.AnimatorInsectoid;
import fuzs.fantasticwings.common.client.model.AvianWingsModel;
import fuzs.fantasticwings.common.client.model.InsectoidWingsModel;
import fuzs.fantasticwings.common.client.model.WingsModel;
import fuzs.fantasticwings.common.client.model.geom.ModModelLayers;
import fuzs.fantasticwings.common.client.renderer.entity.state.AvianRenderState;
import fuzs.fantasticwings.common.client.renderer.entity.state.InsectoidRenderState;
import fuzs.fantasticwings.common.flight.apparatus.FlightApparatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public final class WingFormRegistry implements ResourceManagerReloadListener {
    public static final WingFormRegistry INSTANCE = new WingFormRegistry();

    private WingsModel<AvianRenderState> avianWings;
    private WingsModel<InsectoidRenderState> insectoidWings;

    private WingFormRegistry() {
        // NO-OP
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
        this.avianWings = new AvianWingsModel(entityModels.bakeLayer(ModModelLayers.AVIAN_WINGS_MODEL_LAYER));
        this.insectoidWings = new InsectoidWingsModel(entityModels.bakeLayer(ModModelLayers.INSECTOID_WINGS_MODEL_LAYER));
    }

    public WingForm<?, ?> createWings(Holder<FlightApparatus> holder) {
        ResourceKey<FlightApparatus> resourceKey = holder.unwrapKey().orElseThrow();
        return switch (holder.value().model()) {
            case AVIAN -> new WingForm<>(resourceKey, AnimatorAvian::new, AvianRenderState::new, () -> this.avianWings);
            case INSECTOID -> new WingForm<>(resourceKey,
                    AnimatorInsectoid::new,
                    InsectoidRenderState::new,
                    () -> this.insectoidWings);
        };
    }
}
