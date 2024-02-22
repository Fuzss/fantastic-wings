package fuzs.fantasticwings.server.asm;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AnimatePlayerModelEvent extends PlayerEvent {
    private final PlayerModel<?> model;

    private final float ticksExisted;

    private final float pitch;

    public AnimatePlayerModelEvent(Player player, PlayerModel<?> model, float ticksExisted, float pitch) {
        super(player);
        this.model = model;
        this.ticksExisted = ticksExisted;
        this.pitch = pitch;
    }

    public PlayerModel<?> getModel() {
        return this.model;
    }

    public float getTicksExisted() {
        return this.ticksExisted;
    }

    public float getPitch() {
        return this.pitch;
    }
}
