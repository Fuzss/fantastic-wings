package fuzs.fantasticwings.mixin.client;

import fuzs.fantasticwings.client.handler.ClientEventHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
abstract class EntityMixin {

    @Inject(method = "turn", at = @At("TAIL"))
    public void turn(double yRot, double xRot, CallbackInfo callback) {
        float deltaYaw = (float) yRot * 0.15F;
        ClientEventHandler.onTurn(Entity.class.cast(this), deltaYaw);
    }
}
