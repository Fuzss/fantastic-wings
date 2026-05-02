package fuzs.fantasticwings.common.client.animator;

public interface Animator<S> {

    void beginLand();

    void beginGlide();

    void beginIdle();

    void beginLift();

    void beginFall();

    void update();

    void extractRenderState(S renderState, float partialTick);
}
