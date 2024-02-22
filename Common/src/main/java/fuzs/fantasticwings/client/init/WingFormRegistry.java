package fuzs.fantasticwings.client.init;

import fuzs.fantasticwings.client.apparatus.WingForm;
import fuzs.fantasticwings.client.flight.Animator;
import fuzs.fantasticwings.client.flight.AnimatorAvian;
import fuzs.fantasticwings.client.flight.AnimatorInsectoid;
import fuzs.fantasticwings.client.model.ModelWings;
import fuzs.fantasticwings.client.model.ModelWingsAvian;
import fuzs.fantasticwings.client.model.ModelWingsInsectoid;
import fuzs.fantasticwings.server.flight.apparatus.FlightApparatusImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.function.Supplier;

public class WingFormRegistry implements ResourceManagerReloadListener {
    public static final WingFormRegistry INSTANCE = new WingFormRegistry();

    private ModelWings<AnimatorAvian> avianWings;
    private ModelWings<AnimatorInsectoid> insectoidWings;

    private WingFormRegistry() {
        // NO-OP
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.avianWings = new ModelWingsAvian();
        this.insectoidWings = new ModelWingsInsectoid();
    }

    public void registerAll() {
        WingForm.register(FlightApparatusImpl.ANGEL, this::createAvianWings);
        WingForm.register(FlightApparatusImpl.PARROT, this::createAvianWings);
        WingForm.register(FlightApparatusImpl.BAT, this::createAvianWings);
        WingForm.register(FlightApparatusImpl.BLUE_BUTTERFLY, this::createInsectoidWings);
        WingForm.register(FlightApparatusImpl.DRAGON, this::createAvianWings);
        WingForm.register(FlightApparatusImpl.EVIL, this::createAvianWings);
        WingForm.register(FlightApparatusImpl.FAIRY, this::createInsectoidWings);
        WingForm.register(FlightApparatusImpl.FIRE, this::createAvianWings);
        WingForm.register(FlightApparatusImpl.MONARCH_BUTTERFLY, this::createInsectoidWings);
        WingForm.register(FlightApparatusImpl.SLIME, this::createInsectoidWings);
    }

    private WingForm<AnimatorAvian> createAvianWings(ResourceLocation resourceLocation) {
        return this.createWings(resourceLocation, AnimatorAvian::new, () -> this.avianWings);
    }

    private WingForm<AnimatorInsectoid> createInsectoidWings(ResourceLocation resourceLocation) {
        return this.createWings(resourceLocation, AnimatorInsectoid::new, () -> this.insectoidWings);
    }

    private <A extends Animator> WingForm<A> createWings(ResourceLocation resourceLocation, Supplier<A> animator, Supplier<ModelWings<A>> model) {
        ResourceLocation textureLocation = new ResourceLocation(resourceLocation.getNamespace(),
                "textures/entity/" + resourceLocation.getPath() + ".png"
        );
        return WingForm.of(animator, model, textureLocation);
    }
}
