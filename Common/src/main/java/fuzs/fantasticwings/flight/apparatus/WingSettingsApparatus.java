package fuzs.fantasticwings.flight.apparatus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface WingSettingsApparatus extends FlightApparatus {

    WingSettings getWingSettings();

    @Override
    default void onFlying(Player player, Vec3 direction) {
        int distance = Math.round((float) direction.length() * 100.0F);
        if (distance > 0) {
            player.causeFoodExhaustion(distance * this.getWingSettings().getExhaustionFromFlying());
        }
    }

    @Override
    default void onSlowlyDescending(Player player, Vec3 direction) {
        player.causeFoodExhaustion(this.getWingSettings().getExhaustionFromSlowlyDescending());
    }

    @Override
    default boolean isUsableForFlying(Player player) {
        return player.getAbilities().invulnerable || player.getFoodData().getFoodLevel() >= this.getWingSettings()
                .getRequiredFoodLevelForFlying();
    }

    @Override
    default boolean isUsableForSlowlyDescending(Player player) {
        return player.getAbilities().invulnerable || player.getFoodData().getFoodLevel() >= this.getWingSettings()
                .getRequiredFoodLevelForSlowlyDescending();
    }
}
