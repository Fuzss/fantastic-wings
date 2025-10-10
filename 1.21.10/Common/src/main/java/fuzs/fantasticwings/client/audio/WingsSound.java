package fuzs.fantasticwings.client.audio;

import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class WingsSound extends AbstractTickableSoundInstance {
    private final Player player;
    private final FlightCapability flight;

    public WingsSound(Player player, FlightCapability flight) {
        this(player, flight, true, 0, Math.nextAfter(0.0F, 1.0D));
    }

    private WingsSound(Player player, FlightCapability flight, boolean repeat, int repeatDelay, float volume) {
        super(ModRegistry.ITEM_WINGS_FLYING.value(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.flight = flight;
        this.looping = repeat;
        this.delay = repeatDelay;
        this.volume = volume;
    }

    @Override
    public void tick() {
        if (!this.player.isAlive()) {
            this.stop();
        } else if (this.flight.getFlyingAmount(1.0F) > 0.0F) {
            this.x = (float) this.player.getX();
            this.y = (float) this.player.getY();
            this.z = (float) this.player.getZ();
            float velocity = (float) this.player.getDeltaMovement().length();
            if (velocity >= 0.01F) {
                float halfVel = velocity * 0.5F;
                this.volume = Mth.clamp(halfVel * halfVel, 0.0F, 1.0F);
            } else {
                this.volume = 0.0F;
            }
            final float cutoff = 0.8F;
            if (this.volume > cutoff) {
                this.pitch = 1.0F + (this.volume - cutoff);
            } else {
                this.pitch = 1.0F;
            }
        } else {
            this.volume = 0.0F;
        }
    }
}
