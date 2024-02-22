package fuzs.fantasticwings.server.flight.apparatus;

import fuzs.fantasticwings.FantasticWings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public interface FlightApparatus {
    FlightApparatus NONE = new FlightApparatus() {

        @Override
        public void onFlight(Player player, Vec3 direction) {
            // NO-OP
        }

        @Override
        public void onLanding(Player player, Vec3 direction) {
            // NO-OP
        }

        @Override
        public boolean isUsable(Player player) {
            return false;
        }

        @Override
        public boolean isLandable(Player player) {
            return false;
        }
    };

    void onFlight(Player player, Vec3 direction);

    void onLanding(Player player, Vec3 direction);

    boolean isUsable(Player player);

    boolean isLandable(Player player);

    record Holder(FlightApparatus flightApparatus, boolean isEmpty) {
        private static final String KEY_IS_EMPTY = FantasticWings.id("is_empty").toString();
        private static final String KEY_FLIGHT_APPARATUS = FantasticWings.id("flight_apparatus").toString();
        private static final Holder EMPTY = new Holder(NONE, true);

        public Holder {
            Objects.requireNonNull(flightApparatus, "flight apparatus is null");
        }

        public static Holder of(FlightApparatus flightApparatus) {
            return new Holder(flightApparatus, false);
        }

        public static Holder empty() {
            return EMPTY;
        }

        public boolean is(FlightApparatus otherFlightApparatus) {
            return this.flightApparatus == otherFlightApparatus;
        }

        public boolean is(Holder other) {
            return this.flightApparatus == other.flightApparatus();
        }

        public CompoundTag writeToNBTTag() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putBoolean(KEY_IS_EMPTY, this.isEmpty);
            if (!this.isEmpty) {
                compoundTag.putByte(KEY_FLIGHT_APPARATUS, (byte) ((FlightApparatusImpl) this.flightApparatus).ordinal());
            }
            return compoundTag;
        }

        public static Holder readFromNBTTag(CompoundTag compoundTag) {
            boolean isEmpty = compoundTag.getBoolean(KEY_IS_EMPTY);
            if (!isEmpty) {
                return new Holder(FlightApparatusImpl.byId(compoundTag.getByte(KEY_FLIGHT_APPARATUS)), false);
            } else {
                return empty();
            }
        }
    }
}
