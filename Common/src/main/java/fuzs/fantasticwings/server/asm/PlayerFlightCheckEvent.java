package fuzs.fantasticwings.server.asm;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerFlightCheckEvent extends PlayerEvent {
    private boolean flying;

    public PlayerFlightCheckEvent(Player player) {
        super(player);
    }

    public boolean isFlying() {
        return this.flying;
    }

    public void setFlying() {
        this.flying = true;
    }
}
