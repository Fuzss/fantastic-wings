package fuzs.fantasticwings.client.flight.state;

import fuzs.fantasticwings.client.flight.Animator;

public final class StateLand extends State {
    public StateLand() {
        super(Animator::beginLand);
    }

    @Override
    protected State createLand() {
        return this;
    }
}
