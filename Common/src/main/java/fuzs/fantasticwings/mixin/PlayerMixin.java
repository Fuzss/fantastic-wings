package fuzs.fantasticwings.mixin;

import fuzs.fantasticwings.init.ModCapabilities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    protected void updatePlayerPose(CallbackInfo callback) {
        if (this.canPlayerFitWithinBlocksAndEntitiesWhen(Pose.SWIMMING)) {
            if (ModCapabilities.FLIGHT_CAPABILITY.get(Player.class.cast(this)).isFlying()) {
                this.setPose(Pose.FALL_FLYING);
                callback.cancel();
            }
        }
    }

    @Shadow
    abstract boolean canPlayerFitWithinBlocksAndEntitiesWhen(Pose pose);
}
