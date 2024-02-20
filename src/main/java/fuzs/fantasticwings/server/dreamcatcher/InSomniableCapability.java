package fuzs.fantasticwings.server.dreamcatcher;

import fuzs.fantasticwings.WingsMod;
import fuzs.fantasticwings.util.CapabilityHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WingsMod.ID)
public final class InSomniableCapability {
    private InSomniableCapability() {
    }

    private static final CapabilityHolder<Player, InSomniable, CapabilityHolder.State<Player, InSomniable>> INSOMNIABLE = CapabilityHolder.create();

    public static LazyOptional<InSomniable> getInSomniable(Player player) {
        return INSOMNIABLE.state().get(player, null);
    }

    @CapabilityInject(InSomniable.class)
    static void injectInSomniable(Capability<InSomniable> capability) {
        INSOMNIABLE.inject(capability);
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(
                new ResourceLocation(WingsMod.ID, "insomniable"),
                INSOMNIABLE.state().providerBuilder(new InSomniable())
                    .serializedBy(new InSomniable.Serializer())
                    .build()
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        getInSomniable(event.getOriginal())
            .ifPresent(oldInstance -> getInSomniable(event.getPlayer())
                .ifPresent(newInstance -> newInstance.clone(oldInstance))
            );
    }
}
