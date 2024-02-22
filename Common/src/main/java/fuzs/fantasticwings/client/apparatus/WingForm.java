package fuzs.fantasticwings.client.apparatus;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.flight.Animator;
import fuzs.fantasticwings.client.model.ModelWings;
import fuzs.fantasticwings.server.flight.apparatus.FlightApparatus;
import fuzs.fantasticwings.server.flight.apparatus.FlightApparatusImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class WingForm<A extends Animator> {
    private static final Map<FlightApparatus, WingForm<?>> FORMS = new HashMap<>();

    private final Supplier<A> animator;
    private final Supplier<ModelWings<A>> model;

    private final ResourceLocation textureLocation;

    private WingForm(Supplier<A> animator, Supplier<ModelWings<A>> model, ResourceLocation textureLocation) {
        this.animator = animator;
        this.model = model;
        this.textureLocation = textureLocation;
    }

    public A createAnimator() {
        return this.animator.get();
    }

    public ModelWings<A> getModel() {
        return this.model.get();
    }

    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    public static <A extends Animator> WingForm<A> of(Supplier<A> animator, Supplier<ModelWings<A>> model, ResourceLocation textureLocation) {
        return new WingForm<>(animator, model, textureLocation);
    }

    public static Optional<WingForm<?>> get(FlightApparatus wings) {
        return Optional.ofNullable(FORMS.get(wings));
    }

    public static void register(FlightApparatusImpl wings, Function<ResourceLocation, WingForm<?>> form) {
        FORMS.put(wings, form.apply(wings.resourceLocation()));
    }

    public interface FormRenderer {

        ResourceLocation getTexture();

        void render(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, float delta);
    }
}
