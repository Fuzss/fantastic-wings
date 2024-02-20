package me.paulf.wings.server.asm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;

public final class PlayerFlownEvent extends PlayerEvent {
    private final Vec3 direction;

    public PlayerFlownEvent(Player player, Vec3 direction) {
        super(player);
        this.direction = direction;
    }

    public Vec3 getDirection() {
        return this.direction;
    }
}
