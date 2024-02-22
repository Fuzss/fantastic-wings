package fuzs.fantasticwings.init;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import fuzs.puzzleslib.api.capability.v3.data.SyncStrategy;
import net.minecraft.world.entity.player.Player;

public class ModCapabilities {
    static final CapabilityController CAPABILITIES = CapabilityController.from(FantasticWings.MOD_ID);
    public static final EntityCapabilityKey<Player, FlightCapability> FLIGHT_CAPABILITY = CAPABILITIES.registerEntityCapability(
            "flight",
            FlightCapability.class,
            FlightCapability::new,
            Player.class
    ).setSyncStrategy(SyncStrategy.TRACKING);

    public static void touch() {
        // NO-OP
    }
}
