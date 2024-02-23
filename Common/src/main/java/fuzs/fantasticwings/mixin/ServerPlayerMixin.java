package fuzs.fantasticwings.mixin;

import com.mojang.authlib.GameProfile;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(
            method = "checkMovementStatistics",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/stats/Stats;AVIATE_ONE_CM:Lnet/minecraft/resources/ResourceLocation;"
                    )
            )
    )
    public void checkMovementStatistics(double dx, double dy, double dz, CallbackInfo callback) {
        ModRegistry.FLIGHT_CAPABILITY.get(this).onFlown(new Vec3(dx, dy, dz));
    }
}
