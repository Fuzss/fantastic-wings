package fuzs.fantasticwings.client.init;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.flight.FlightView;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ClientModRegistry {
    public static final KeyMapping FLY_KEY_MAPPING = KeyMappingHelper.registerKeyMapping(FantasticWings.id(
            "toggle_flight"), InputConstants.KEY_R);

    public static final DataAttachmentType<Entity, FlightView> FLIGHT_VIEW_ATTACHMENT_TYPE = DataAttachmentRegistry.<FlightView>entityBuilder()
            .defaultValue(EntityType.PLAYER, FlightView.VOID)
            .build(FantasticWings.id("flight_view"));

    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(FantasticWings.MOD_ID);
    public static final ModelLayerLocation INSECTOID_WINGS = MODEL_LAYERS.register("insectoid_wings");
    public static final ModelLayerLocation AVIAN_WINGS = MODEL_LAYERS.register("avian_wings");

    public static void bootstrap() {
        // NO-OP
    }
}
