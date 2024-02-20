package me.paulf.wings.server.asm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ApplyPlayerRotationsEvent extends PlayerEvent {
    private final PoseStack matrixStack;

    private final float delta;

    public ApplyPlayerRotationsEvent(Player player, PoseStack matrixStack, float delta) {
        super(player);
        this.matrixStack = matrixStack;
        this.delta = delta;
    }

    public PoseStack getMatrixStack() {
        return this.matrixStack;
    }

    public float getDelta() {
        return this.delta;
    }
}
