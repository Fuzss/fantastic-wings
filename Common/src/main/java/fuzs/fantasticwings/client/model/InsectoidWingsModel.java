package fuzs.fantasticwings.client.model;

import fuzs.fantasticwings.client.renderer.entity.state.InsectoidRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class InsectoidWingsModel extends WingsModel<InsectoidRenderState> {
    private final ModelPart wingLeft;
    private final ModelPart wingRight;

    public InsectoidWingsModel(ModelPart modelPart) {
        super(modelPart);
        this.wingLeft = modelPart.getChild("left_wing");
        this.wingRight = modelPart.getChild("right_wing");
    }

    public static LayerDefinition createWingsLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -8.0F, 0.0F, 19.0F, 24.0F, 0.0F),
                PartPose.offset(0.0F, 2.0F, 2.5F));
        partDefinition.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(0, 24).addBox(-19.0F, -8.0F, 0.0F, 19.0F, 24.0F, 0.0F),
                PartPose.offset(0.0F, 2.0F, 2.5F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(InsectoidRenderState renderState) {
        super.setupAnim(renderState);
        setAngles(this.wingLeft, this.wingRight, renderState.angles);
    }
}
