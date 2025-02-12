package fuzs.fantasticwings.client.animator.state;

import fuzs.fantasticwings.client.animator.Animator;

public final class StateLand extends State {

    public StateLand() {
        super(Animator::beginLand);
    }

    @Override
    protected State createLand() {
        return this;
    }
}
