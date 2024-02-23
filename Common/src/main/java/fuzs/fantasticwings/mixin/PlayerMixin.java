package fuzs.fantasticwings.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.fantasticwings.init.ModRegistry;
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

    @WrapOperation(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isFallFlying()Z"))
    protected boolean updatePlayerPose$0(Player player, Operation<Boolean> operation) {
        return operation.call(player) || ModRegistry.FLIGHT_CAPABILITY.get(Player.class.cast(this)).isFlying();
    }

    @WrapOperation(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isShiftKeyDown()Z"))
    protected boolean updatePlayerPose$1(Player player, Operation<Boolean> operation) {
        // crouching increases falling speed when not flying but having wings,
        // treat this just like creative mode descending where the pose and therefore eye height is not offset for crouching
        if (!ModRegistry.FLIGHT_CAPABILITY.get(Player.class.cast(this)).getWings().isEmpty() && this.getDeltaMovement().y() < -0.5) {
            return false;
        } else {
            return operation.call(player);
        }
    }
}
