package fuzs.fantasticwings.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fuzs.fantasticwings.common.flight.Flight;
import fuzs.fantasticwings.common.init.ModRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "getDesiredPose",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/entity/player/Player;isFallFlying()Z"))
    protected boolean getDesiredPose$0(boolean isFallFlying) {
        return isFallFlying || ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(Player.class.cast(this), Flight.VOID)
                .isFlying();
    }

    @ModifyExpressionValue(method = "getDesiredPose",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/entity/player/Player;isShiftKeyDown()Z"))
    protected boolean getDesiredPose$1(boolean isShiftKeyDown) {
        // Crouching increases falling speed when not flying but having wings.
        // Treat this just like creative mode descending where both pose and therefore eye height are not offset for crouching.
        return isShiftKeyDown && (
                ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(Player.class.cast(this), Flight.VOID).isEmpty()
                        || this.getDeltaMovement().y() >= -0.5);
    }
}
