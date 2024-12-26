package fuzs.fantasticwings.client.flight.apparatus;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.animator.Animator;
import fuzs.fantasticwings.client.model.WingsModel;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.flight.apparatus.FlightApparatusImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class WingForm<A extends Animator> {
    private static final Map<FlightApparatus, WingForm<?>> FORMS = new HashMap<>();

    private final Supplier<A> animator;
    private final Supplier<WingsModel<A>> model;
    private final ResourceLocation textureLocation;

    private WingForm(Supplier<A> animator, Supplier<WingsModel<A>> model, ResourceLocation textureLocation) {
        this.animator = animator;
        this.model = model;
        this.textureLocation = textureLocation;
    }

    public A createAnimator() {
        return this.animator.get();
    }

    public WingsModel<A> getModel() {
        return this.model.get();
    }

    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    public static <A extends Animator> WingForm<A> of(Supplier<A> animator, Supplier<WingsModel<A>> model, ResourceLocation textureLocation) {
        return new WingForm<>(animator, model, textureLocation);
    }

    public static Optional<WingForm<?>> get(FlightApparatus wings) {
        return Optional.ofNullable(FORMS.get(wings));
    }

    public static void register(FlightApparatusImpl wings, Function<ResourceLocation, WingForm<?>> form) {
        FORMS.put(wings, form.apply(wings.resourceLocation()));
    }

    public interface FormRenderer {

        ResourceLocation getTextureLocation();

        void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color, float partialTick);
    }
}
