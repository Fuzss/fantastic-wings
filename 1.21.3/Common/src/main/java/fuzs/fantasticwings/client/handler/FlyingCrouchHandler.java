package fuzs.fantasticwings.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FlyingCrouchHandler {

    public static EventResult onBeforeRenderPlayer(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (mustPreventCrouchingOffset(player)) {
            renderer.getModel().crouching = false;
            Vec3 vec3 = renderer.getRenderOffset((AbstractClientPlayer) player, partialTick);
            poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
        }

        return EventResult.PASS;
    }

    private static boolean mustPreventCrouchingOffset(Player player) {
        if (player.isCrouching()) {
            FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(player);
            // crouching increases falling speed when not flying but having wings,
            // treat this just like creative mode descending where the player model is not offset for crouching
            return flightCapability.isFlying() || !flightCapability.getWings().isEmpty() && player.getDeltaMovement()
                    .y() < -0.5;
        }

        return false;
    }

    public static void onAfterRenderPlayer(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (mustPreventCrouchingOffset(player)) {
            Vec3 vec3 = renderer.getRenderOffset((AbstractClientPlayer) player, partialTick);
            poseStack.translate(vec3.x(), vec3.y(), vec3.z());
        }
    }
}
