package fuzs.fantasticwings.server.net.clientbound;

import fuzs.fantasticwings.server.flight.Flight;
import fuzs.fantasticwings.server.flight.FlightDefault;
import fuzs.fantasticwings.server.flight.Flights;
import fuzs.fantasticwings.server.net.ClientMessageContext;
import fuzs.fantasticwings.server.net.Message;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

public final class MessageSyncFlight implements Message {
    private int playerId;

    private final Flight flight;

    public MessageSyncFlight() {
        this(0, new FlightDefault());
    }

    public MessageSyncFlight(Player player, Flight flight) {
        this(player.getId(), flight);
    }

    private MessageSyncFlight(int playerId, Flight flight) {
        this.playerId = playerId;
        this.flight = flight;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.playerId);
        this.flight.serialize(buf);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.playerId = buf.readVarInt();
        this.flight.deserialize(buf);
    }

    public static void handle(MessageSyncFlight message, ClientMessageContext context) {
        Flights.ifPlayer(context.getWorld().getEntity(message.playerId),
            (player, flight) -> flight.clone(message.flight)
        );
    }
}
