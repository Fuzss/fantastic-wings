package fuzs.fantasticwings.client.model;

import fuzs.fantasticwings.util.MathHelper;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;


public abstract class WingsModel<S> extends Model<S> {

    public WingsModel(ModelPart root) {
        super(root, RenderType::entityCutout);
    }

    static void setAngles(ModelPart left, ModelPart right, Vec3 angles) {
        right.xRot = (left.xRot = MathHelper.toRadians((float) angles.x));
        right.yRot = -(left.yRot = MathHelper.toRadians((float) angles.y));
        right.zRot = -(left.zRot = MathHelper.toRadians((float) angles.z));
    }
}
