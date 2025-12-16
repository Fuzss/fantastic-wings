package fuzs.fantasticwings.client.animator.state;

import fuzs.fantasticwings.client.animator.Animator;

public final class StateGlide extends State {
    private static final int LIFT_STATE_DELAY = 6;

    public StateGlide() {
        super(Animator::beginGlide);
    }

    @Override
    protected State createLift() {
        return new StateLift(LIFT_STATE_DELAY);
    }

    @Override
    protected State createGlide() {
        return this;
    }
}
