package me.paulf.wings.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.paulf.wings.client.flight.Animator;
import me.paulf.wings.util.Mth;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec3;


public abstract class ModelWings<A extends Animator> extends Model {
    public ModelWings() {
        super(RenderType::entityCutout);
    }

    @Deprecated
    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    }

    public abstract void render(A animator, float delta, PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha);

    static void setAngles(ModelPart left, ModelPart right, Vec3 angles) {
        right.xRot = (left.xRot = Mth.toRadians((float) angles.x));
        right.yRot = -(left.yRot = Mth.toRadians((float) angles.y));
        right.zRot = -(left.zRot = Mth.toRadians((float) angles.z));
    }
}
