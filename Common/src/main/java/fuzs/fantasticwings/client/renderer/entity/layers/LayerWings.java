package fuzs.fantasticwings.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.init.ModClientCapabilities;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;

public class LayerWings extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public LayerWings(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, EntityRendererProvider.Context rendererContext) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float age, float headYaw, float headPitch) {
        if (!player.isInvisible()) {
            ModClientCapabilities.FLIGHT_VIEW_CAPABILITY.get(player).ifFormPresent(form -> {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(form.getTexture()));
                poseStack.pushPose();
                this.applyRenderOffset(poseStack, player);
                form.render(poseStack,
                        vertexConsumer,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F,
                        partialTick
                );
                poseStack.popPose();
            });
        }
    }

    private void applyRenderOffset(PoseStack poseStack, Player player) {
        if (player.isCrouching()) {
            poseStack.translate(0.0D, 0.2D, 0.0D);
        }
        this.getParentModel().body.translateAndRotate(poseStack);
    }
}
