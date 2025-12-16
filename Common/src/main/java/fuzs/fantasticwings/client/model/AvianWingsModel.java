package fuzs.fantasticwings.client.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import fuzs.fantasticwings.client.model.geom.builders.FlatCubeListBuilder;
import fuzs.fantasticwings.client.renderer.entity.state.AvianRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.phys.Vec3;

public final class AvianWingsModel extends WingsModel<AvianRenderState> {
    public static final int BONES = 4;
    public static final int FEATHERS = 4;

    private final ImmutableList<ModelPart> bonesLeft, bonesRight;
    private final ImmutableList<ModelPart> feathersLeft, feathersRight;

    public AvianWingsModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart leftCoracoid = modelPart.getChild("left_coracoid");
        ModelPart rightCoracoid = modelPart.getChild("right_coracoid");
        ModelPart leftHumerus = leftCoracoid.getChild("left_humerus");
        ModelPart rightHumerus = rightCoracoid.getChild("right_humerus");
        ModelPart leftUlna = leftHumerus.getChild("left_ulna");
        ModelPart rightUlna = rightHumerus.getChild("right_ulna");
        ModelPart leftCarpals = leftUlna.getChild("left_carpals");
        ModelPart rightCarpals = rightUlna.getChild("right_carpals");
        ModelPart leftCoracoidFeathers = leftCoracoid.getChild("left_coracoid_feathers");
        ModelPart rightCoracoidFeathers = rightCoracoid.getChild("right_coracoid_feathers");
        ModelPart leftTertiaryFeathers = leftHumerus.getChild("left_tertiary_feathers");
        ModelPart rightTertiaryFeathers = rightHumerus.getChild("right_tertiary_feathers");
        ModelPart leftSecondaryFeathers = leftUlna.getChild("left_secondary_feathers");
        ModelPart rightSecondaryFeathers = rightUlna.getChild("right_secondary_feathers");
        ModelPart leftPrimaryFeathers = leftCarpals.getChild("left_primary_feathers");
        ModelPart rightPrimaryFeathers = rightCarpals.getChild("right_primary_feathers");
        this.bonesLeft = ImmutableList.of(leftCoracoid, leftHumerus, leftUlna, leftCarpals);
        this.bonesRight = ImmutableList.of(rightCoracoid, rightHumerus, rightUlna, rightCarpals);
        this.feathersLeft = ImmutableList.of(leftCoracoidFeathers,
                leftTertiaryFeathers,
                leftSecondaryFeathers,
                leftPrimaryFeathers);
        this.feathersRight = ImmutableList.of(rightCoracoidFeathers,
                rightTertiaryFeathers,
                rightSecondaryFeathers,
                rightPrimaryFeathers);
        Preconditions.checkState(this.bonesLeft.size() == BONES && this.bonesRight.size() == BONES);
        Preconditions.checkState(this.feathersLeft.size() == FEATHERS && this.feathersRight.size() == FEATHERS);
    }

    public static LayerDefinition createWingsLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition leftCoracoid = root.addOrReplaceChild("left_coracoid",
                CubeListBuilder.create().texOffs(0, 28).addBox(0.0F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F),
                PartPose.offset(1.5F, 5.5F, 2.5F));
        PartDefinition rightCoracoid = root.addOrReplaceChild("right_coracoid",
                CubeListBuilder.create().texOffs(0, 34).addBox(-5.0F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F),
                PartPose.offset(-1.5F, 5.5F, 2.5F));
        PartDefinition leftHumerus = leftCoracoid.addOrReplaceChild("left_humerus",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.1F, -1.1F, -2.0F, 7.0F, 3.0F, 4.0F),
                PartPose.offset(4.7F, -0.6F, 0.1F));
        PartDefinition rightHumerus = rightCoracoid.addOrReplaceChild("right_humerus",
                CubeListBuilder.create().texOffs(0, 7).addBox(-6.9F, -1.1F, -2.0F, 7.0F, 3.0F, 4.0F),
                PartPose.offset(-4.7F, -0.6F, 0.1F));
        PartDefinition leftUlna = leftHumerus.addOrReplaceChild("left_ulna",
                CubeListBuilder.create().texOffs(22, 0).addBox(0.0F, -1.5F, -1.5F, 9.0F, 3.0F, 3.0F),
                PartPose.offset(6.5F, 0.2F, 0.1F));
        PartDefinition rightUlna = rightHumerus.addOrReplaceChild("right_ulna",
                CubeListBuilder.create().texOffs(22, 6).addBox(-9.0F, -1.5F, -1.5F, 9.0F, 3.0F, 3.0F),
                PartPose.offset(-6.5F, 0.2F, 0.1F));
        PartDefinition leftCarpals = leftUlna.addOrReplaceChild("left_carpals",
                CubeListBuilder.create().texOffs(46, 0).addBox(0.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offset(8.5F, 0, 0));
        PartDefinition rightCarpals = rightUlna.addOrReplaceChild("right_carpals",
                CubeListBuilder.create().texOffs(46, 4).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offset(-8.5F, 0, 0));
        leftCoracoid.addOrReplaceChild("left_coracoid_feathers",
                FlatCubeListBuilder.create().texOffs(6, 40).addBox(0.0F, 0.0F, -1.0F, 6.0F, 8.0F, 1.0F),
                PartPose.offset(0.4F, 0, 1));
        rightCoracoid.addOrReplaceChild("right_coracoid_feathers",
                FlatCubeListBuilder.create().texOffs(0, 40).addBox(-6.0F, 0.0F, -1.0F, 6.0F, 8.0F, 1.0F),
                PartPose.offset(-0.4F, 0, 1));
        leftHumerus.addOrReplaceChild("left_tertiary_feathers",
                FlatCubeListBuilder.create().texOffs(10, 14).addBox(0.0F, 0.0F, -0.5F, 10.0F, 14.0F, 1.0F),
                PartPose.offset(0, 1.5F, 1));
        rightHumerus.addOrReplaceChild("right_tertiary_feathers",
                FlatCubeListBuilder.create().texOffs(0, 14).addBox(-10.0F, 0.0F, -0.5F, 10.0F, 14.0F, 1.0F),
                PartPose.offset(0, 1.5F, 1));
        leftUlna.addOrReplaceChild("left_secondary_feathers",
                FlatCubeListBuilder.create().texOffs(31, 14).addBox(-2.0F, 0.0F, -0.5F, 11.0F, 12.0F, 1.0F),
                PartPose.offset(0, 1, 0));
        rightUlna.addOrReplaceChild("right_secondary_feathers",
                FlatCubeListBuilder.create().texOffs(20, 14).addBox(-9.0F, 0.0F, -0.5F, 11.0F, 12.0F, 1.0F),
                PartPose.offset(0, 1, 0));
        leftCarpals.addOrReplaceChild("left_primary_feathers",
                FlatCubeListBuilder.create().texOffs(53, 14).addBox(0.0F, -2.1F, -0.5F, 11.0F, 11.0F, 1.0F),
                PartPose.ZERO);
        rightCarpals.addOrReplaceChild("right_primary_feathers",
                FlatCubeListBuilder.create().texOffs(42, 14).addBox(-11.0F, -2.1F, -0.5F, 11.0F, 11.0F, 1.0F),
                PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(AvianRenderState renderState) {
        super.setupAnim(renderState);
        for (int i = 0; i < BONES; i++) {
            ModelPart left = this.bonesLeft.get(i);
            ModelPart right = this.bonesRight.get(i);
            setAngles(left, right, renderState.wingAngles.getOrDefault(i, Vec3.ZERO));
        }

        for (int i = 0; i < FEATHERS; i++) {
            ModelPart left = this.feathersLeft.get(i);
            ModelPart right = this.feathersRight.get(i);
            setAngles(left, right, renderState.featherAngles.getOrDefault(i, Vec3.ZERO));
        }
    }
}
