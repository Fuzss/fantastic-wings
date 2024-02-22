package fuzs.fantasticwings.client.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(FantasticWings.MOD_ID);
    public static final ModelLayerLocation AVIAN_WINGS = MODEL_LAYERS.register("avian_wings");
    public static final ModelLayerLocation INSECTOID_WINGS = MODEL_LAYERS.register("insectoid_wings");
}
