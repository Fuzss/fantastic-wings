package me.paulf.wings.server.net;

import me.paulf.wings.WingsMod;
import me.paulf.wings.server.net.clientbound.MessageSyncFlight;
import me.paulf.wings.server.net.serverbound.MessageControlFlying;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class Network {
    private final SimpleChannel network = new NetBuilder(new ResourceLocation(WingsMod.ID, "net"))
        .version(1).optionalServer().requiredClient()
        .serverbound(MessageControlFlying::new).consumer(() -> MessageControlFlying::handle)
        .clientbound(MessageSyncFlight::new).consumer(() -> MessageSyncFlight::handle)
        .build();

    public void sendToServer(Message message) {
        network.sendToServer(message);
    }

    public void sendToPlayer(Message message, ServerPlayer player) {
        network.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public void sendToAllTracking(Message message, Entity entity) {
        network.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
