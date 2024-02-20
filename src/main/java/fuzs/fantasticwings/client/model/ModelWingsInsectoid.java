package fuzs.fantasticwings.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.flight.AnimatorInsectoid;
import net.minecraft.client.model.geom.ModelPart;

public final class ModelWingsInsectoid extends ModelWings<AnimatorInsectoid> {
    private final ModelPart root;

    private final ModelPart wingLeft;

    private final ModelPart wingRight;

    public ModelWingsInsectoid() {
        this.texWidth = this.texHeight = 64;
        this.root = new ModelPart(this, 0, 0);
        this.wingLeft = new ModelPart(this, 0, 0);
        this.wingLeft.setPos(0, 2, 3.5F);
        this.wingLeft.addBox(0, -8, 0, 19, 24, 0, 0);
        this.wingRight = new ModelPart(this, 0, 24);
        this.wingRight.setPos(0, 2, 3.5F);
        this.wingRight.addBox(-19, -8, 0, 19, 24, 0, 0);
        this.root.addChild(this.wingLeft);
        this.root.addChild(this.wingRight);
    }

    @Override
    public void render(AnimatorInsectoid animator, float delta, PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        setAngles(this.wingLeft, this.wingRight, animator.getRotation(delta));
        this.root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
