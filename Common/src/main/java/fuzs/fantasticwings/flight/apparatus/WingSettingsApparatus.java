package fuzs.fantasticwings.flight.apparatus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface WingSettingsApparatus extends FlightApparatus {

    WingSettings getWingSettings();

    @Override
    default void onFlight(Player player, Vec3 direction) {
        int distance = Math.round((float) direction.length() * 100.0F);
        if (distance > 0) {
            player.causeFoodExhaustion(distance * this.getWingSettings().getFlyingExertion());
        }
    }

    @Override
    default void onLanding(Player player, Vec3 direction) {
        player.causeFoodExhaustion(this.getWingSettings().getLandingExertion());
    }

    @Override
    default boolean isUsable(Player player) {
        return player.getFoodData().getFoodLevel() >= this.getWingSettings().getRequiredFlightSatiation();
    }

    @Override
    default boolean isLandable(Player player) {
        return player.getFoodData().getFoodLevel() >= this.getWingSettings().getRequiredLandSatiation();
    }
}
