package me.paulf.wings.server.net;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;

public class ServerMessageContext extends MessageContext {
    public ServerMessageContext(NetworkEvent.Context context) {
        super(context);
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }

    public MinecraftServer getServer() {
        return this.getPlayer().server;
    }

    public ServerLevel getWorld() {
        return this.getPlayer().getLevel();
    }

    public ServerPlayer getPlayer() {
        return Objects.requireNonNull(this.context.getSender());
    }
}
