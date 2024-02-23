package fuzs.fantasticwings.config;

import fuzs.fantasticwings.flight.apparatus.WingSettings;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config
    public final WingSettingsConfig angelWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig parrotWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig slimeWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig blueButterflyWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig monarchButterflyWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig fireWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig batWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig fairyWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig evilWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig dragonWings = new WingSettingsConfig();
    @Config
    public final WingSettingsConfig metallicWings = new WingSettingsConfig();

    public static final class WingSettingsConfig implements WingSettings, ConfigCore {
        @Config(description = "Minimum amount of saturation required to be allowed to start and continue to fly.")
        @Config.IntRange(min = 0, max = 20)
        int requiredFoodLevelForFlying = 6;
        @Config(description = "Exhaustion caused from continuously flying.")
        @Config.DoubleRange(min = 0.0, max = 10.0)
        double exhaustionFromFlying = 0.0001;
        @Config(description = "Minimum amount of saturation required to be able to slowly descent back to the ground.")
        @Config.IntRange(min = 0, max = 20)
        int requiredFoodLevelForSlowlyDescending = 2;
        @Config(description = "Exhaustion caused from slowly descending back to the ground.")
        @Config.DoubleRange(min = 0.0D, max = 10.0D)
        double exhaustionFromSlowlyDescending = 0.005;

        @Override
        public int getRequiredFoodLevelForFlying() {
            return this.requiredFoodLevelForFlying;
        }

        @Override
        public float getExhaustionFromFlying() {
            return (float) this.exhaustionFromFlying;
        }

        @Override
        public int getRequiredFoodLevelForSlowlyDescending() {
            return this.requiredFoodLevelForSlowlyDescending;
        }

        @Override
        public float getExhaustionFromSlowlyDescending() {
            return (float) this.exhaustionFromSlowlyDescending;
        }
    }
}
