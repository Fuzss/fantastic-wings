package fuzs.fantasticwings.client.flight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.animator.Animator;
import fuzs.fantasticwings.client.flight.apparatus.WingForm;
import fuzs.fantasticwings.client.animator.state.State;
import fuzs.fantasticwings.client.animator.state.StateIdle;
import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public final class FlightViewCapability extends CapabilityComponent<AbstractClientPlayer> {
    private WingState animator = PresentWingState.VOID;

    public void ifFormPresent(Consumer<WingForm.FormRenderer> consumer) {
        this.animator.ifFormPresent(consumer);
    }

    public void tick() {
        this.animator = WingForm.get(this.getFlight().getWings().flightApparatus())
                .map(view -> this.animator.next(view))
                .orElseGet(this.animator::nextAbsent);
        this.animator.update(this.getFlight(), this.getHolder());
    }

    public boolean resetEyeHeight() {
        return this.getFlight().isFlying() || (this.getFlight().getFlyingAmount(1.0F) > 0.0F && this.getHolder().getPose() == Pose.FALL_FLYING);
    }

    public FlightCapability getFlight() {
        return ModCapabilities.FLIGHT_CAPABILITY.get(this.getHolder());
    }

    private interface Strategy {

        void update(FlightCapability flight, Player player);

        void ifFormPresent(Consumer<WingForm.FormRenderer> consumer);
    }

    interface WingState {

        WingState nextAbsent();

        WingState next(WingForm<?> form);

        void update(FlightCapability flight, Player player);

        void ifFormPresent(Consumer<WingForm.FormRenderer> consumer);
    }

    private record PresentWingState(WingForm<?> wing, Strategy behavior) implements WingState {
        static final WingState VOID = new WingState() {
            @Override
            public WingState nextAbsent() {
                return this;
            }

            @Override
            public WingState next(WingForm<?> form) {
                return PresentWingState.newState(form);
            }

            @Override
            public void update(FlightCapability flight, Player player) {
                // NO-OP
            }

            @Override
            public void ifFormPresent(Consumer<WingForm.FormRenderer> consumer) {
                // NO-OP
            }
        };

        @Override
        public WingState nextAbsent() {
            return VOID;
        }

        @Override
        public WingState next(WingForm<?> form) {
            if (this.wing.equals(form)) {
                return this;
            } else {
                return PresentWingState.newState(form);
            }
        }

        @Override
        public void update(FlightCapability flight, Player player) {
            this.behavior.update(flight, player);
        }

        @Override
        public void ifFormPresent(Consumer<WingForm.FormRenderer> consumer) {
            this.behavior.ifFormPresent(consumer);
        }

        public static <T extends Animator> WingState newState(WingForm<T> shape) {
            return new PresentWingState(shape, new WingStrategy<>(shape));
        }

        private static class WingStrategy<T extends Animator> implements Strategy {
            private final WingForm<T> shape;
            private final T animator;
            private State state;

            public WingStrategy(WingForm<T> shape) {
                this.shape = shape;
                this.animator = shape.createAnimator();
                this.state = new StateIdle();
            }

            @Override
            public void update(FlightCapability flight, Player player) {
                this.animator.update();
                State state = this.state.update(flight,
                        player.getX() - player.xo,
                        player.getY() - player.yo,
                        player.getZ() - player.zo,
                        player
                );
                if (!this.state.equals(state)) {
                    state.beginAnimation(this.animator);
                }
                this.state = state;
            }

            @Override
            public void ifFormPresent(Consumer<WingForm.FormRenderer> consumer) {
                consumer.accept(new WingForm.FormRenderer() {
                    @Override
                    public ResourceLocation getTexture() {
                        return WingStrategy.this.shape.getTextureLocation();
                    }

                    @Override
                    public void render(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, float delta) {
                        WingStrategy.this.shape.getModel()
                                .render(WingStrategy.this.animator,
                                        delta,
                                        matrixStack,
                                        buffer,
                                        packedLight,
                                        packedOverlay,
                                        red,
                                        green,
                                        blue,
                                        alpha
                                );
                    }
                });
            }
        }
    }
}
