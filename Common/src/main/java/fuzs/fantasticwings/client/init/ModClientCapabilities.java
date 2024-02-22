package fuzs.fantasticwings.client.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.capability.client.FlightViewCapability;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import net.minecraft.client.player.AbstractClientPlayer;

public class ModClientCapabilities {
    static final CapabilityController CAPABILITIES = CapabilityController.from(FantasticWings.MOD_ID);
    public static final EntityCapabilityKey<AbstractClientPlayer, FlightViewCapability> FLIGHT_VIEW_CAPABILITY = CAPABILITIES.registerEntityCapability(
            "flight_view",
            FlightViewCapability.class,
            FlightViewCapability::new,
            AbstractClientPlayer.class
    );

    public static void touch() {
        // NO-OP
    }
}
