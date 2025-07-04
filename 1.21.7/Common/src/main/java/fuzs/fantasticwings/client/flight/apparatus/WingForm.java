package fuzs.fantasticwings.client.flight.apparatus;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.client.animator.Animator;
import fuzs.fantasticwings.client.model.WingsModel;
import fuzs.fantasticwings.flight.apparatus.FlightApparatus;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public final class WingForm<A extends Animator> {
    private final ResourceKey<FlightApparatus> resourceKey;
    private final Supplier<A> animator;
    private final Supplier<WingsModel<A>> model;
    private final ResourceLocation textureLocation;

    public WingForm(ResourceKey<FlightApparatus> resourceKey, Supplier<A> animator, Supplier<WingsModel<A>> model) {
        this.resourceKey = resourceKey;
        this.animator = animator;
        this.model = model;
        this.textureLocation = FlightApparatus.transformTextureLocation(FlightApparatus.getTextureLocation(resourceKey));
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

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof WingForm<?> wingForm && wingForm.resourceKey == this.resourceKey;
    }

    public static Optional<WingForm<?>> get(Holder<FlightApparatus> holder) {
        return Optional.of(WingFormRegistry.INSTANCE.createWings(holder));
    }

    public interface FormRenderer {

        ResourceLocation getTextureLocation();

        void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color, float partialTick);
    }
}
