package me.paulf.wings.client.flight.state;

import me.paulf.wings.client.flight.Animator;
import me.paulf.wings.server.flight.Flight;
import me.paulf.wings.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public abstract class State {
    static final int STATE_DELAY = 2;

    private final int stateDelay;

    private final Consumer<Animator> animation;

    private int time;

    protected State(Consumer<Animator> animation) {
        this(STATE_DELAY, animation);
    }

    protected State(int stateDelay, Consumer<Animator> animation) {
        this.stateDelay = stateDelay;
        this.animation = animation;
    }

    public final State update(Flight flight, double x, double y, double z, Player player) {
        if (this.time++ > this.stateDelay) {
            return this.getNext(flight, x, y, z, player);
        }
        return this;
    }

    private State getNext(Flight flight, double x, double y, double z, Player player) {
        if (flight.isFlying()) {
            if (y < 0 && player.xRot >= this.getPitch(x, y, z)) {
                return this.createGlide();
            }
            return this.createLift();
        }
        if (y < 0) {
            return this.getDescent(flight, player);
        }
        return this.getDefault(y);
    }

    private float getPitch(double x, double y, double z) {
        return Mth.toDegrees((float) -Math.atan2(y, Mth.sqrt(x * x + z * z)));
    }

    public final void beginAnimation(Animator animator) {
        this.animation.accept(animator);
    }

    protected State createLand() {
        return new StateLand();
    }

    protected State createLift() {
        return new StateLift();
    }

    protected State createGlide() {
        return new StateGlide();
    }

    protected State createIdle() {
        return new StateIdle();
    }

    protected State createFall() {
        return new StateFall();
    }

    protected State getDefault(double y) {
        return this.createIdle();
    }

    protected State getDescent(Flight flight, Player player) {
        return flight.canLand(player) ? this.createLand() : this.createFall();
    }
}
