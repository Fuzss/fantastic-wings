package fuzs.fantasticwings.common.client.model.geom;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.puzzleslib.common.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(FantasticWings.MOD_ID);
    public static final ModelLayerLocation INSECTOID_WINGS_MODEL_LAYER = MODEL_LAYERS.registerModelLayer(
            "insectoid_wings");
    public static final ModelLayerLocation AVIAN_WINGS_MODEL_LAYER = MODEL_LAYERS.registerModelLayer("avian_wings");
}
