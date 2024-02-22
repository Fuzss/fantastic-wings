package fuzs.fantasticwings.client.animator.state;

import fuzs.fantasticwings.client.animator.Animator;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.util.MathHelper;
import net.minecraft.world.entity.player.Player;

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

    public final State update(FlightCapability flight, double x, double y, double z, Player player) {
        if (this.time++ > this.stateDelay) {
            return this.getNext(flight, x, y, z, player);
        }
        return this;
    }

    private State getNext(FlightCapability flight, double x, double y, double z, Player player) {
        if (flight.isFlying()) {
            if (y < 0 && player.getXRot() >= this.getPitch(x, y, z)) {
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
        return MathHelper.toDegrees((float) -Math.atan2(y, Math.sqrt(x * x + z * z)));
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

    protected State getDescent(FlightCapability flight, Player player) {
        return flight.canLand() ? this.createLand() : this.createFall();
    }
}
