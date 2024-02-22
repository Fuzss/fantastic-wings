package fuzs.fantasticwings.client.flight.state;

import fuzs.fantasticwings.client.flight.Animator;

public final class StateFall extends State {

    public StateFall() {
        super(Animator::beginFall);
    }

    @Override
    protected State createFall() {
        return this;
    }
}
