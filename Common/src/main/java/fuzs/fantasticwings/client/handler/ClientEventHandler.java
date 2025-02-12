package fuzs.fantasticwings.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.audio.WingsSound;
import fuzs.fantasticwings.client.flight.FlightView;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.fantasticwings.util.MathHelper;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ClientEventHandler {
    public static final RenderPropertyKey<FlightView> FLIGHT_VIEW_RENDER_PROPERTY_KEY = new RenderPropertyKey<>(
            FantasticWings.id("flight_view"));
    public static final RenderPropertyKey<Float> FLYING_AMOUNT_RENDER_PROPERTY_KEY = new RenderPropertyKey<>(
            FantasticWings.id("flying_amount"));
    public static final RenderPropertyKey<Float> ROLL_RENDER_PROPERTY_KEY = new RenderPropertyKey<>(FantasticWings.id(
            "roll"));
    public static final RenderPropertyKey<Float> PITCH_RENDER_PROPERTY_KEY = new RenderPropertyKey<>(FantasticWings.id(
            "pitch"));

    public static void setupPlayerAnim(PlayerRenderState renderState, HumanoidModel<PlayerRenderState> model) {
        float flyingAmount = RenderPropertyKey.getRenderProperty(renderState, FLYING_AMOUNT_RENDER_PROPERTY_KEY);
        if (flyingAmount != 0.0F) {
            model.head.xRot = MathHelper.toRadians(MathHelper.lerp(renderState.xRot,
                    renderState.xRot / 4.0F - 90.0F,
                    flyingAmount));
            model.leftArm.xRot = MathHelper.lerp(model.leftArm.xRot, -3.2F, flyingAmount);
            model.rightArm.xRot = MathHelper.lerp(model.rightArm.xRot, -3.2F, flyingAmount);
            model.leftLeg.xRot = MathHelper.lerp(model.leftLeg.xRot, 0.0F, flyingAmount);
            model.rightLeg.xRot = MathHelper.lerp(model.rightLeg.xRot, 0.0F, flyingAmount);
            model.hat.copyFrom(model.head);
        }
    }

    public static void setupPlayerRotations(PlayerRenderState renderState, PoseStack poseStack) {
        float flyingAmount = RenderPropertyKey.getRenderProperty(renderState, FLYING_AMOUNT_RENDER_PROPERTY_KEY);
        if (flyingAmount > 0.0F) {
            float roll = RenderPropertyKey.getRenderProperty(renderState, ROLL_RENDER_PROPERTY_KEY);
            float pitch = RenderPropertyKey.getRenderProperty(renderState, PITCH_RENDER_PROPERTY_KEY);
            poseStack.mulPose(Axis.ZP.rotationDegrees(MathHelper.lerpDegrees(0.0F, roll, flyingAmount)));
            poseStack.mulPose(Axis.XP.rotationDegrees(MathHelper.lerpDegrees(0.0F, pitch, flyingAmount)));
            poseStack.translate(0.0, -1.2 * MathHelper.easeInOut(flyingAmount), 0.0);
        }
    }

    public static void onComputeCameraAngles(GameRenderer renderer, Camera camera, float partialTick, MutableFloat pitch, MutableFloat yaw, MutableFloat roll) {
        LivingEntity cameraEntity = (LivingEntity) camera.getEntity();
        ModRegistry.FLIGHT_CAPABILITY.getIfProvided(cameraEntity).ifPresent(flightViewCapability -> {
            float flyingAmount = flightViewCapability.getFlyingAmount(partialTick);
            if (flyingAmount > 0.0F) {
                float newRoll = MathHelper.lerpDegrees(cameraEntity.yBodyRotO - cameraEntity.yRotO,
                        cameraEntity.yBodyRot - cameraEntity.getYRot(),
                        partialTick);
                roll.accept(MathHelper.lerpDegrees(0.0F, -newRoll * 0.25F, flyingAmount));
            }
        });
    }

    public static EventResult onEntityLoad(Entity entity, ClientLevel level) {
        if (entity instanceof LocalPlayer localPlayer) {
            FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(localPlayer);
            Minecraft.getInstance().getSoundManager().play(new WingsSound(localPlayer, flightCapability));
        }

        return EventResult.PASS;
    }

    public static void onExtractRenderState(Entity entity, EntityRenderState entityRenderState, float partialTick) {
        if (entity instanceof AbstractClientPlayer player &&
                entityRenderState instanceof PlayerRenderState playerRenderState) {
            RenderPropertyKey.setRenderProperty(entityRenderState,
                    FLIGHT_VIEW_RENDER_PROPERTY_KEY,
                    ClientModRegistry.FLIGHT_VIEW_ATTACHMENT_TYPE.get(entity));
            RenderPropertyKey.setRenderProperty(entityRenderState,
                    FLYING_AMOUNT_RENDER_PROPERTY_KEY,
                    ModRegistry.FLIGHT_CAPABILITY.get(player).getFlyingAmount(partialTick));
            float roll = MathHelper.lerpDegrees(player.yBodyRotO - player.yRotO,
                    player.yBodyRot - player.getYRot(),
                    partialTick);
            RenderPropertyKey.setRenderProperty(entityRenderState, ROLL_RENDER_PROPERTY_KEY, roll);
            float pitch = -MathHelper.lerpDegrees(player.xRotO, player.getXRot(), partialTick) - 90.0F;
            RenderPropertyKey.setRenderProperty(entityRenderState, PITCH_RENDER_PROPERTY_KEY, pitch);
            if (mustPreventCrouchingOffset(player)) {
                playerRenderState.isCrouching = false;
            }
        }
    }

    private static boolean mustPreventCrouchingOffset(Player player) {
        if (player.isCrouching()) {
            FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(player);
            // crouching increases falling speed when not flying but having wings,
            // treat this just like creative mode descending where the player model is not offset for crouching
            return flightCapability.isFlying() ||
                    !flightCapability.getWings().isEmpty() && player.getDeltaMovement().y() < -0.5;
        }

        return false;
    }

    public static EventResult onRenderOffHand(ItemInHandRenderer itemInHandRenderer, InteractionHand interactionHand, AbstractClientPlayer player, HumanoidArm humanoidArm, ItemStack itemStack, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress) {
        if (itemStack.isEmpty() && !player.isScoping() && !player.isInvisible()) {
            if (!itemInHandRenderer.mainHandItem.is(Items.FILLED_MAP) &&
                    ModRegistry.FLIGHT_CAPABILITY.get(player).isFlying()) {
                itemInHandRenderer.renderPlayerArm(poseStack,
                        bufferSource,
                        combinedLight,
                        equipProgress,
                        swingProgress,
                        player.getMainArm().getOpposite());
                return EventResult.INTERRUPT;
            }
        }
        return EventResult.PASS;
    }

    public static void onTurn(Entity entity, float deltaYaw) {
        if (entity instanceof Player player && ModRegistry.FLIGHT_CAPABILITY.get(player).isFlying()) {
            float theta = Mth.wrapDegrees(player.getYRot() - player.yBodyRot);
            if (theta < -50.0F || theta > 50.0F) {
                player.yBodyRot += deltaYaw;
                player.yBodyRotO += deltaYaw;
            }
        }
    }
}
