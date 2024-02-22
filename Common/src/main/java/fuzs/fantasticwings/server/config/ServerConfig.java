package fuzs.fantasticwings.server.config;

import fuzs.fantasticwings.server.flight.apparatus.WingSettings;
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

    public static final class WingSettingsConfig implements WingSettings, ConfigCore {
        @Config.IntRange(min = 0, max = 20)
        int requiredFlightSatiation = 5;
        @Config.DoubleRange(min = 0.0, max = 10.0)
        double flyingExertion = 0.0001;
        @Config.IntRange(min = 0, max = 20)
        int requiredLandSatiation = 2;
        @Config.DoubleRange(min = 0.0D, max = 10.0D)
        double landingExertion = 0.005;

        @Override
        public int getRequiredFlightSatiation() {
            return this.requiredFlightSatiation;
        }

        @Override
        public float getFlyingExertion() {
            return (float) this.flyingExertion;
        }

        @Override
        public int getRequiredLandSatiation() {
            return this.requiredLandSatiation;
        }

        @Override
        public float getLandingExertion() {
            return (float) this.landingExertion;
        }
    }
}
