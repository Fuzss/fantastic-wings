package fuzs.fantasticwings.common.network;

import fuzs.fantasticwings.common.flight.Flight;
import fuzs.fantasticwings.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.common.api.network.v4.message.play.ServerboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundControlFlyingMessage(boolean isFlying) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundControlFlyingMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ServerboundControlFlyingMessage::isFlying,
            ServerboundControlFlyingMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                ServerPlayer player = context.player();
                Flight flight = ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(player, Flight.VOID);
                if (flight.canFly(player) || !ServerboundControlFlyingMessage.this.isFlying) {
                    flight = flight.setIsFlying(player, ServerboundControlFlyingMessage.this.isFlying);
                    ModRegistry.FLIGHT_ATTACHMENT_TYPE.set(player, flight);
                }
            }
        };
    }
}
