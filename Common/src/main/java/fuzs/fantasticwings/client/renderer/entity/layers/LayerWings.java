package fuzs.fantasticwings.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

public class LayerWings extends RenderLayer<PlayerRenderState, PlayerModel> {

    public LayerWings(RenderLayerParent<PlayerRenderState, PlayerModel> renderLayerParent, EntityRendererProvider.Context rendererContext) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, PlayerRenderState renderState, float yRot, float xRot) {
        if (!renderState.isInvisible && !renderState.chestItem.is(ModRegistry.WING_OBSTRUCTIONS)) {
            RenderPropertyKey.getRenderProperty(renderState, ClientEventHandler.FLIGHT_VIEW_RENDER_PROPERTY_KEY)
                    .ifFormPresent(form -> {
                        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(form.getTextureLocation()));
                        poseStack.pushPose();
                        poseStack.translate(0.0, -0.0625, 0.0);
                        if (!renderState.chestItem.isEmpty()) {
                            poseStack.translate(0.0, 0.0, 0.0625);
                        }
                        this.getParentModel().body.translateAndRotate(poseStack);
                        form.render(poseStack,
                                vertexConsumer,
                                packedLight,
                                OverlayTexture.NO_OVERLAY,
                                -1,
                                Mth.frac(renderState.ageInTicks));
                        poseStack.popPose();
                    });
        }
    }
}
