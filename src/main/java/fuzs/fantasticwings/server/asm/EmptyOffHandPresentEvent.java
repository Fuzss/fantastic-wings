package fuzs.fantasticwings.server.asm;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.eventbus.api.Event;

@Event.HasResult
public final class EmptyOffHandPresentEvent extends Event {
    private final LocalPlayer player;

    public EmptyOffHandPresentEvent(LocalPlayer player) {
        this.player = player;
    }

    public LocalPlayer getPlayer() {
        return this.player;
    }
}
