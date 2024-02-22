package fuzs.fantasticwings.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FlyingCrouchHandler {

    public static EventResult onBeforeRenderPlayer(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (ModCapabilities.FLIGHT_CAPABILITY.get(player).isFlying() && player.isCrouching()) {
            renderer.getModel().crouching = false;
            Vec3 vec3 = renderer.getRenderOffset((AbstractClientPlayer) player, partialTick);
            poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
        }
        return EventResult.PASS;
    }

    public static void onAfterRenderPlayer(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (ModCapabilities.FLIGHT_CAPABILITY.get(player).isFlying() && player.isCrouching()) {
            Vec3 vec3 = renderer.getRenderOffset((AbstractClientPlayer) player, partialTick);
            poseStack.translate(vec3.x(), vec3.y(), vec3.z());
        }
    }
}
