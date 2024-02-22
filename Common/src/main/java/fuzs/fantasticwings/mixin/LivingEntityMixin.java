package fuzs.fantasticwings.mixin;

import fuzs.fantasticwings.init.ModCapabilities;
import fuzs.fantasticwings.handler.ServerEventHandler;
import fuzs.fantasticwings.flight.FlightCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isVisuallySwimming", at = @At("HEAD"), cancellable = true)
    public void isVisuallySwimming(CallbackInfoReturnable<Boolean> callback) {
        if (!super.isVisuallySwimming() && ModCapabilities.FLIGHT_CAPABILITY.getIfProvided(this).filter(FlightCapability::isFlying).isPresent()) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "tickHeadTurn", at = @At("HEAD"))
    protected void tickHeadTurn(float yRot, float animStep, CallbackInfoReturnable<Float> callback) {
        if (ServerEventHandler.onUpdateBodyRotation(LivingEntity.class.cast(this), yRot)) {
            callback.setReturnValue(0.0F);
        }
    }
}
