package fuzs.fantasticwings.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.fantasticwings.capability.client.FlightViewCapability;
import fuzs.fantasticwings.client.audio.WingsSound;
import fuzs.fantasticwings.client.init.ModClientCapabilities;
import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.server.asm.EmptyOffHandPresentEvent;
import fuzs.fantasticwings.server.flight.FlightCapability;
import fuzs.fantasticwings.server.flight.Flights;
import fuzs.fantasticwings.util.MathHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ClientEventHandler {

    public static void onAnimatePlayerModel(Player player, PlayerModel<?> model, float ticksExisted, float pitch) {
        FlightCapability flightCapability = ModCapabilities.FLIGHT_CAPABILITY.get(player);
        float delta = ticksExisted - player.tickCount;
        float flyingAmount = flightCapability.getFlyingAmount(delta);
        if (flyingAmount != 0.0F) {
            model.head.xRot = MathHelper.toRadians(MathHelper.lerp(pitch, pitch / 4.0F - 90.0F, flyingAmount));
            model.leftArm.xRot = MathHelper.lerp(model.leftArm.xRot, -3.2F, flyingAmount);
            model.rightArm.xRot = MathHelper.lerp(model.rightArm.xRot, -3.2F, flyingAmount);
            model.leftLeg.xRot = MathHelper.lerp(model.leftLeg.xRot, 0.0F, flyingAmount);
            model.rightLeg.xRot = MathHelper.lerp(model.rightLeg.xRot, 0.0F, flyingAmount);
            model.hat.copyFrom(model.head);
        }
    }

    public static void onApplyRotations(Player player, PoseStack poseStack, float delta) {
        FlightCapability flightCapability = ModCapabilities.FLIGHT_CAPABILITY.get(player);
        float amt = flightCapability.getFlyingAmount(delta);
        if (amt > 0.0F) {
            float roll = MathHelper.lerpDegrees(
                    player.yBodyRotO - player.yRotO,
                    player.yBodyRot - player.getYRot(),
                    delta
            );
            float pitch = -MathHelper.lerpDegrees(player.xRotO, player.getXRot(), delta) - 90.0F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(MathHelper.lerpDegrees(0.0F, roll, amt)));
            poseStack.mulPose(Axis.XP.rotationDegrees(MathHelper.lerpDegrees(0.0F, pitch, amt)));
            poseStack.translate(0.0D, -1.2D * MathHelper.easeInOut(amt), 0.0D);
        }
    }

    public static void onComputeCameraAngles(GameRenderer renderer, Camera camera, float partialTick, MutableFloat pitch, MutableFloat yaw, MutableFloat roll) {
        LivingEntity cameraEntity = (LivingEntity) camera.getEntity();
        ModCapabilities.FLIGHT_CAPABILITY.getIfProvided(cameraEntity).ifPresent(flightViewCapability -> {
            float flyingAmount = flightViewCapability.getFlyingAmount(partialTick);
            if (flyingAmount > 0.0F) {
                float newRoll = MathHelper.lerpDegrees(
                        cameraEntity.yBodyRotO - cameraEntity.yRotO,
                        cameraEntity.yBodyRot - cameraEntity.getYRot(),
                        partialTick
                );
                roll.accept(MathHelper.lerpDegrees(0.0F, -newRoll * 0.25F, flyingAmount));
            }
        });
    }

    @SubscribeEvent
    public static void onEmptyOffHandPresentEvent(EmptyOffHandPresentEvent event) {
        Flights.get(event.getPlayer()).ifPresent(flight -> {
            if (flight.isFlying()) {
                event.setResult(Event.Result.ALLOW);
            }
        });
    }

    public static EventResult onEntityLoad(Entity entity, ClientLevel level) {
        if (entity instanceof LocalPlayer localPlayer) {
            FlightCapability flightCapability = ModCapabilities.FLIGHT_CAPABILITY.get(localPlayer);
            Minecraft.getInstance().getSoundManager().play(new WingsSound(localPlayer, flightCapability));
        }

        return EventResult.PASS;
    }

    public static void onEndPlayerTick(Player player) {
        ModClientCapabilities.FLIGHT_VIEW_CAPABILITY.getIfProvided(player).ifPresent(FlightViewCapability::tick);
    }
}
