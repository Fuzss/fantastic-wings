package fuzs.fantasticwings.client.flight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.animator.Animator;
import fuzs.fantasticwings.client.animator.state.State;
import fuzs.fantasticwings.client.animator.state.StateIdle;
import fuzs.fantasticwings.client.flight.apparatus.WingForm;
import fuzs.fantasticwings.client.init.ClientModRegistry;
import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record FlightView(WingState animator) {
    public static final FlightView VOID = new FlightView(PresentWingState.VOID);

    public void ifFormPresent(Consumer<WingForm.FormRenderer> consumer) {
        this.animator.ifFormPresent(consumer);
    }

    public static void onEndPlayerTick(Player player) {
        ClientModRegistry.FLIGHT_VIEW_ATTACHMENT_TYPE.update(player, (FlightView flightView) -> {
            return flightView.tick(player);
        });
    }

    private FlightView tick(Player player) {
        WingState animator = this.getFlight(player)
                .wings()
                .flatMap(WingForm::get)
                .map(this.animator::next)
                .orElseGet(this.animator::nextAbsent);
        animator.update(this.getFlight(player), player);
        return new FlightView(animator);
    }

    public FlightCapability getFlight(Player player) {
        return ModRegistry.FLIGHT_CAPABILITY.get(player);
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
                        player);
                if (!this.state.equals(state)) {
                    state.beginAnimation(this.animator);
                }
                this.state = state;
            }

            @Override
            public void ifFormPresent(Consumer<WingForm.FormRenderer> consumer) {
                consumer.accept(new WingForm.FormRenderer() {
                    @Override
                    public ResourceLocation getTextureLocation() {
                        return WingStrategy.this.shape.getTextureLocation();
                    }

                    @Override
                    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color, float partialTick) {
                        WingStrategy.this.shape.getModel().setupAnim(WingStrategy.this.animator, partialTick);
                        WingStrategy.this.shape.getModel()
                                .renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
                    }
                });
            }
        }
    }
}
