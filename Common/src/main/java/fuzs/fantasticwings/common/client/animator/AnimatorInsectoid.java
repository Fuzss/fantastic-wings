package fuzs.fantasticwings.common.client.animator;

import fuzs.fantasticwings.common.client.renderer.entity.state.InsectoidRenderState;
import fuzs.fantasticwings.common.util.MathHelper;
import net.minecraft.world.phys.Vec3;

public final class AnimatorInsectoid implements Animator<InsectoidRenderState> {
    private static final float IDLE_FLAP_RATE = 0.05F;
    private static final float LIFT_FLAP_RATE = 1.2F;
    private float targetFlapRate = IDLE_FLAP_RATE;

    private float flapRate;
    private float prevFlapCycle;
    private float flapCycle;

    @Override
    public void beginLand() {
        this.beginLift();
    }

    @Override
    public void beginGlide() {
        this.beginLift();
    }

    @Override
    public void beginIdle() {
        this.targetFlapRate = IDLE_FLAP_RATE;
    }

    @Override
    public void beginLift() {
        this.targetFlapRate = LIFT_FLAP_RATE;
    }

    @Override
    public void beginFall() {
        this.beginIdle();
    }

    public Vec3 getRotation(float delta) {
        return new Vec3(0.0D,
                Math.sin(MathHelper.lerp(this.prevFlapCycle, this.flapCycle, delta)) * 35.0D - 42.0D,
                0.0D);
    }

    @Override
    public void update() {
        this.prevFlapCycle = this.flapCycle;
        this.flapCycle += this.flapRate;
        this.flapRate += (this.targetFlapRate - this.flapRate) * 0.4F;
    }

    @Override
    public void extractRenderState(InsectoidRenderState renderState, float partialTick) {
        renderState.angles = this.getRotation(partialTick);
    }
}
