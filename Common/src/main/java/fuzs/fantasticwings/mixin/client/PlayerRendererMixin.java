package fuzs.fantasticwings.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
abstract class PlayerRendererMixin<E extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<E, AvatarRenderState, PlayerModel> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    protected void setupRotations(AvatarRenderState renderState, PoseStack poseStack, float bodyRot, float scale, CallbackInfo callback) {
        ClientEventHandler.setupPlayerRotations(renderState, poseStack);
    }
}
