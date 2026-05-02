package fuzs.fantasticwings.common.mixin;

import com.mojang.authlib.GameProfile;
import fuzs.fantasticwings.common.flight.Flight;
import fuzs.fantasticwings.common.init.ModRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "checkMovementStatistics",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"),
            slice = @Slice(from = @At(value = "FIELD",
                                      target = "Lnet/minecraft/stats/Stats;AVIATE_ONE_CM:Lnet/minecraft/resources/Identifier;",
                                      opcode = Opcodes.GETSTATIC)))
    public void checkMovementStatistics(double dx, double dy, double dz, CallbackInfo callback) {
        ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(this, Flight.VOID).onFlown(this, new Vec3(dx, dy, dz));
    }
}
