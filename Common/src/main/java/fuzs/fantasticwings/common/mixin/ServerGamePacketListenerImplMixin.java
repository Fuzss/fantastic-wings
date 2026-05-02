package fuzs.fantasticwings.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.fantasticwings.common.flight.Flight;
import fuzs.fantasticwings.common.init.ModRegistry;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
abstract class ServerGamePacketListenerImplMixin extends ServerCommonPacketListenerImpl {

    public ServerGamePacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }

    @WrapOperation(method = "handleMovePlayer",
                   at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isFallFlying()Z"))
    public boolean handleMovePlayer(ServerPlayer serverPlayer, Operation<Boolean> operation) {
        // disables server-side movement checks when flying just like for elytra gliding
        return operation.call(serverPlayer) || ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(serverPlayer,
                Flight.VOID).isFlying();
    }
}
