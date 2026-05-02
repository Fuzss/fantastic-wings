package fuzs.fantasticwings.common.client.audio;

import fuzs.fantasticwings.common.flight.Flight;
import fuzs.fantasticwings.common.init.ModRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * @see net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance
 */
public class WingsOnPlayerSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;

    public WingsOnPlayerSoundInstance(Player player) {
        this(player, true, 0, Math.nextAfter(0.0F, 1.0D));
    }

    private WingsOnPlayerSoundInstance(Player player, boolean repeat, int repeatDelay, float volume) {
        super(ModRegistry.ITEM_WINGS_FLYING.value(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = repeat;
        this.delay = repeatDelay;
        this.volume = volume;
    }

    @Override
    public void tick() {
        if (this.player.isRemoved()) {
            this.stop();
        } else if (ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(this.player, Flight.VOID).getFlyingAmount(1.0F)
                > 0.0F) {
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
