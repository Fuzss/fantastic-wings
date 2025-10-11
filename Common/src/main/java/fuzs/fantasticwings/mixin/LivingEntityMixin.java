package fuzs.fantasticwings.mixin;

import fuzs.fantasticwings.flight.Flight;
import fuzs.fantasticwings.handler.ServerEventHandler;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isVisuallySwimming", at = @At("HEAD"), cancellable = true)
    public void isVisuallySwimming(CallbackInfoReturnable<Boolean> callback) {
        if (!super.isVisuallySwimming()) {
            if (ModRegistry.FLIGHT_ATTACHMENT_TYPE.getOrDefault(this, Flight.VOID).isFlying()) {
                callback.setReturnValue(false);
            }
        }
    }

    @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
    protected void tickHeadTurn(float yRot, CallbackInfo callback) {
        if (ServerEventHandler.onUpdateBodyRotation(LivingEntity.class.cast(this), yRot)) {
            callback.cancel();
        }
    }
}
