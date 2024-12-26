package fuzs.fantasticwings.client.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.animator.AnimatorAvian;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelWingsAvian extends ModelWings<AnimatorAvian> {
    private final ModelPart root;
    private final ImmutableList<ModelPart> bonesLeft, bonesRight;
    private final ImmutableList<ModelPart> feathersLeft, feathersRight;

    public ModelWingsAvian(ModelPart modelPart) {
        this.root = modelPart;
        ModelPart leftCoracoid = modelPart.getChild("left_coracoid");
        ModelPart rightCoracoid = modelPart.getChild("right_coracoid");
        ModelPart leftHumerus = leftCoracoid.getChild("left_humerus");
        ModelPart rightHumerus = rightCoracoid.getChild("right_humerus");
        ModelPart leftUlna = leftHumerus.getChild("left_ulna");
        ModelPart rightUlna = rightHumerus.getChild("right_ulna");
        ModelPart leftCarpals = leftUlna.getChild("left_carpals");
        ModelPart rightCarpals = rightUlna.getChild("right_carpals");
        ModelPart leftCoracoidFeathers = leftCoracoid.getChild("left_coracoid_feathers");
        add3DTexture(leftCoracoidFeathers, 6, 40, 0, 0, -1, 6, 8);
        ModelPart rightCoracoidFeathers = rightCoracoid.getChild("right_coracoid_feathers");
        add3DTexture(rightCoracoidFeathers, 0, 40, -6, 0, -1, 6, 8);
        ModelPart leftTertiaryFeathers = leftHumerus.getChild("left_tertiary_feathers");
        add3DTexture(leftTertiaryFeathers, 10, 14, 0, 0, -0.5F, 10, 14);
        ModelPart rightTertiaryFeathers = rightHumerus.getChild("right_tertiary_feathers");
        add3DTexture(rightTertiaryFeathers, 0, 14, -10, 0, -0.5F, 10, 14);
        ModelPart leftSecondaryFeathers = leftUlna.getChild("left_secondary_feathers");
        add3DTexture(leftSecondaryFeathers, 31, 14, -2, 0, -0.5F, 11, 12);
        ModelPart rightSecondaryFeathers = rightUlna.getChild("right_secondary_feathers");
        add3DTexture(rightSecondaryFeathers, 20, 14, -9, 0, -0.5F, 11, 12);
        ModelPart leftPrimaryFeathers = leftCarpals.getChild("left_primary_feathers");
        add3DTexture(leftPrimaryFeathers, 53, 14, 0, -2.1F, -0.5F, 11, 11);
        ModelPart rightPrimaryFeathers = rightCarpals.getChild("right_primary_feathers");
        add3DTexture(rightPrimaryFeathers, 42, 14, -11, -2.1F, -0.5F, 11, 11);
        this.bonesLeft = ImmutableList.of(leftCoracoid, leftHumerus, leftUlna, leftCarpals);
        this.bonesRight = ImmutableList.of(rightCoracoid, rightHumerus, rightUlna, rightCarpals);
        this.feathersLeft = ImmutableList.of(leftCoracoidFeathers,
                leftTertiaryFeathers,
                leftSecondaryFeathers,
                leftPrimaryFeathers
        );
        this.feathersRight = ImmutableList.of(rightCoracoidFeathers,
                rightTertiaryFeathers,
                rightSecondaryFeathers,
                rightPrimaryFeathers
        );
    }

    public static LayerDefinition createWingsLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition leftCoracoid = root.addOrReplaceChild("left_coracoid",
                CubeListBuilder.create().texOffs(0, 28).addBox(0, -1.5F, -1.5F, 5, 3, 3),
                PartPose.offset(1.5F, 5.5F, 2.5F)
        );
        PartDefinition rightCoracoid = root.addOrReplaceChild("right_coracoid",
                CubeListBuilder.create().texOffs(0, 34).addBox(-5, -1.5F, -1.5F, 5, 3, 3),
                PartPose.offset(-1.5F, 5.5F, 2.5F)
        );
        PartDefinition leftHumerus = leftCoracoid.addOrReplaceChild("left_humerus",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.1F, -1.1F, -2, 7, 3, 4),
                PartPose.offset(4.7F, -0.6F, 0.1F)
        );
        PartDefinition rightHumerus = rightCoracoid.addOrReplaceChild("right_humerus",
                CubeListBuilder.create().texOffs(0, 7).addBox(-6.9F, -1.1F, -2, 7, 3, 4),
                PartPose.offset(-4.7F, -0.6F, 0.1F)
        );
        PartDefinition leftUlna = leftHumerus.addOrReplaceChild("left_ulna",
                CubeListBuilder.create().texOffs(22, 0).addBox(0, -1.5F, -1.5F, 9, 3, 3),
                PartPose.offset(6.5F, 0.2F, 0.1F)
        );
        PartDefinition rightUlna = rightHumerus.addOrReplaceChild("right_ulna",
                CubeListBuilder.create().texOffs(22, 6).addBox(-9, -1.5F, -1.5F, 9, 3, 3),
                PartPose.offset(-6.5F, 0.2F, 0.1F)
        );
        PartDefinition leftCarpals = leftUlna.addOrReplaceChild("left_carpals",
                CubeListBuilder.create().texOffs(46, 0).addBox(0, -1, -1, 5, 2, 2),
                PartPose.offset(8.5F, 0, 0)
        );
        PartDefinition rightCarpals = rightUlna.addOrReplaceChild("right_carpals",
                CubeListBuilder.create().texOffs(46, 4).addBox(-5, -1, -1, 5, 2, 2),
                PartPose.offset(-8.5F, 0, 0)
        );
        PartDefinition leftCoracoidFeathers = leftCoracoid.addOrReplaceChild("left_coracoid_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0.4F, 0, 1)
        );
        PartDefinition rightCoracoidFeathers = rightCoracoid.addOrReplaceChild("right_coracoid_feathers",
                CubeListBuilder.create(),
                PartPose.offset(-0.4F, 0, 1)
        );
        PartDefinition leftTertiaryFeathers = leftHumerus.addOrReplaceChild("left_tertiary_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0, 1.5F, 1)
        );
        PartDefinition rightTertiaryFeathers = rightHumerus.addOrReplaceChild("right_tertiary_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0, 1.5F, 1)
        );
        PartDefinition leftSecondaryFeathers = leftUlna.addOrReplaceChild("left_secondary_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0, 1, 0)
        );
        PartDefinition rightSecondaryFeathers = rightUlna.addOrReplaceChild("right_secondary_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0, 1, 0)
        );
        PartDefinition leftPrimaryFeathers = leftCarpals.addOrReplaceChild("left_primary_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0, 0, 0)
        );
        PartDefinition rightPrimaryFeathers = rightCarpals.addOrReplaceChild("right_primary_feathers",
                CubeListBuilder.create(),
                PartPose.offset(0, 0, 0)
        );
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void render(AnimatorAvian animator, float delta, PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        for (int i = 0; i < this.bonesLeft.size(); i++) {
            ModelPart left = this.bonesLeft.get(i);
            ModelPart right = this.bonesRight.get(i);
            setAngles(left, right, animator.getWingRotation(i, delta));
        }
        for (int i = 0; i < this.feathersLeft.size(); i++) {
            ModelPart left = this.feathersLeft.get(i);
            ModelPart right = this.feathersRight.get(i);
            setAngles(left, right, animator.getFeatherRotation(i, delta));
        }
        this.root.render(matrixStack, buffer, packedLight, packedOverlay, color);
    }

    private static void add3DTexture(ModelPart model, int u, int v, float offX, float offY, float offZ, int width, int height) {
        Preconditions.checkState(model.cubes.isEmpty(), "model part cube list is not empty");
        model.cubes = ImmutableList.of(Model3DTexture.create(offX, offY, offZ, width, height, u, v, 64, 64));
    }
}
