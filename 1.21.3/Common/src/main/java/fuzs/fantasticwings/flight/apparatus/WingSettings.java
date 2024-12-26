package fuzs.fantasticwings.flight.apparatus;

public interface WingSettings {

    int getRequiredFoodLevelForFlying();

    float getExhaustionFromFlying();

    int getRequiredFoodLevelForSlowlyDescending();

    float getExhaustionFromSlowlyDescending();
}
