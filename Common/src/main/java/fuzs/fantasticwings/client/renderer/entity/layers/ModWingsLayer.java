package fuzs.fantasticwings.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.flight.FlightView;
import fuzs.fantasticwings.client.handler.ClientEventHandler;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ModWingsLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    public ModWingsLayer(RenderLayerParent<PlayerRenderState, PlayerModel> renderLayerParent, EntityRendererProvider.Context context) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, PlayerRenderState renderState, float yRot, float xRot) {
        if (!renderState.isInvisible && !renderState.chestEquipment.is(ModRegistry.WING_OBSTRUCTIONS)) {
            ClientEventHandler.getOrDefault(renderState,
                    ClientEventHandler.FLIGHT_VIEW_RENDER_PROPERTY_KEY,
                    FlightView.VOID).ifFormPresent(form -> {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(form.getTextureLocation()));
                poseStack.pushPose();
                poseStack.translate(0.0, -0.0625, 0.0);
                if (!renderState.chestEquipment.isEmpty()) {
                    poseStack.translate(0.0, 0.0, 0.0625);
                }
                this.getParentModel().body.translateAndRotate(poseStack);
                form.render(poseStack,
                        vertexConsumer,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        -1,
                        ClientAbstractions.INSTANCE.getPartialTick(renderState));
                poseStack.popPose();
            });
        }
    }
}
