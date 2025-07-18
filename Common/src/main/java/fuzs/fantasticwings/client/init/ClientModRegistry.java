package fuzs.fantasticwings.client.init;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.flight.FlightView;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ClientModRegistry {
    public static final KeyMapping FLY_KEY_MAPPING = KeyMappingHelper.registerKeyMapping(FantasticWings.id(
            "toggle_flight"), InputConstants.KEY_R);

    public static final DataAttachmentType<Entity, FlightView> FLIGHT_VIEW_ATTACHMENT_TYPE = DataAttachmentRegistry.<FlightView>entityBuilder()
            .defaultValue(EntityType.PLAYER, FlightView.VOID)
            .build(FantasticWings.id("flight_view"));

    public static void bootstrap() {
        // NO-OP
    }
}
