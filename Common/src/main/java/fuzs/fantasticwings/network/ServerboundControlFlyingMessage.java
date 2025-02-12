package fuzs.fantasticwings.network;

import fuzs.fantasticwings.flight.FlightCapability;
import fuzs.fantasticwings.init.ModRegistry;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public record ServerboundControlFlyingMessage(boolean isFlying) implements ServerboundMessage<ServerboundControlFlyingMessage> {

    @Override
    public ServerMessageListener<ServerboundControlFlyingMessage> getHandler() {
        return new ServerMessageListener<>() {
            @Override
            public void handle(ServerboundControlFlyingMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                FlightCapability flightCapability = ModRegistry.FLIGHT_CAPABILITY.get(player);
                if (flightCapability.canFly() || !message.isFlying) {
                    flightCapability.setIsFlying(message.isFlying, PlayerSet.nearPlayer(player));
                }
            }
        };
    }
}
