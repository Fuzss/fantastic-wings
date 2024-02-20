package fuzs.fantasticwings.server.apparatus;

import fuzs.fantasticwings.server.flight.Flight;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface FlightApparatus {
    FlightApparatus NONE = new FlightApparatus() {
        @Override
        public void onFlight(Player player, Vec3 direction) {
        }

        @Override
        public void onLanding(Player player, Vec3 direction) {
        }

        @Override
        public boolean isUsable(Player player) {
            return true;
        }

        @Override
        public boolean isLandable(Player player) {
            return true;
        }

        @Override
        public FlightState createState(Flight flight) {
            return FlightState.NONE;
        }
    };

    void onFlight(Player player, Vec3 direction);

    void onLanding(Player player, Vec3 direction);

    boolean isUsable(Player player);

    boolean isLandable(Player player);

    FlightState createState(Flight flight);

    interface FlightState {
        FlightState NONE = (player) -> {
        };

        void onUpdate(Player player);
    }
}
