package me.paulf.wings.server.flight;

import me.paulf.wings.WingsMod;
import me.paulf.wings.util.CapabilityHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WingsMod.ID)
public final class Flights {
    private Flights() {
    }

    private static final CapabilityHolder<Player, Flight, CapabilityHolder.State<Player, Flight>> HOLDER = CapabilityHolder.create();

    public static boolean has(Player player) {
        return HOLDER.state().has(player, null);
    }

    public static LazyOptional<Flight> get(Player player) {
        return HOLDER.state().get(player, null);
    }

    @CapabilityInject(Flight.class)
    static void inject(Capability<Flight> capability) {
        HOLDER.inject(capability);
    }

    public static void ifPlayer(Entity entity, BiConsumer<Player, Flight> action) {
        ifPlayer(entity, e -> true, action);
    }

    public static void ifPlayer(Entity entity, Predicate<Player> condition, BiConsumer<Player, Flight> action) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            get(player).filter(f -> condition.test(player))
                .ifPresent(f -> action.accept(player, f));
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            Supplier<FlightDefault> factory = () -> {
                FlightDefault flight = new FlightDefault();
                WingsMod.instance().addFlightListeners((Player) entity, flight);
                return flight;
            };
            FlightDefault flight = factory.get();
            event.addCapability(
                new ResourceLocation(WingsMod.ID, "flight"),
                HOLDER.state().providerBuilder(flight)
                    .serializedBy(new FlightDefault.Serializer(factory))
                    .build()
            );
            MinecraftForge.EVENT_BUS.post(AttachFlightCapabilityEvent.create(event, flight));
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            get(event.getOriginal()).ifPresent(oldInstance ->
                get(event.getPlayer()).ifPresent(newInstance -> newInstance.clone(oldInstance))
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        get(event.getPlayer()).ifPresent(flight -> flight.sync(Flight.PlayerSet.ofSelf()));
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        get(event.getPlayer()).ifPresent(flight -> flight.sync(Flight.PlayerSet.ofSelf()));
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        get(event.getPlayer()).ifPresent(flight -> flight.sync(Flight.PlayerSet.ofSelf()));
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        ifPlayer(event.getTarget(), (player, flight) ->
            flight.sync(Flight.PlayerSet.ofPlayer((ServerPlayer) event.getPlayer()))
        );
    }
}
