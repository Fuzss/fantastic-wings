package fuzs.fantasticwings.server.flight;

import fuzs.fantasticwings.init.ModCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class Flights {

    public static Optional<FlightCapability> get(Player player) {
        return Optional.of(ModCapabilities.FLIGHT_CAPABILITY.get(player));
    }

    public static void ifPlayer(Entity entity, BiConsumer<Player, FlightCapability> action) {
        ifPlayer(entity, e -> true, action);
    }

    public static void ifPlayer(Entity entity, Predicate<Player> condition, BiConsumer<Player, FlightCapability> action) {
        if (entity instanceof Player player) {
            get(player).filter(f -> condition.test(player))
                .ifPresent(f -> action.accept(player, f));
        }
    }
}
