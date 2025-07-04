package fuzs.fantasticwings.client.animator.state;

import fuzs.fantasticwings.client.animator.Animator;

public final class StateFall extends State {

    public StateFall() {
        super(Animator::beginFall);
    }

    @Override
    protected State createFall() {
        return this;
    }
}
