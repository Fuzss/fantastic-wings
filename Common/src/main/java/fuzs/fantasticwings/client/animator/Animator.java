package fuzs.fantasticwings.client.animator;

public interface Animator {

    void beginLand();

    void beginGlide();

    void beginIdle();

    void beginLift();

    void beginFall();

    void update();
}
