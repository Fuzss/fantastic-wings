package fuzs.fantasticwings.mixin.client;

import fuzs.fantasticwings.client.init.ModClientCapabilities;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
abstract class CameraMixin {
    @Shadow
    private Entity entity;
    @Shadow
    private float eyeHeight;
    @Shadow
    private float eyeHeightOld;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callback) {
        if (this.entity instanceof LocalPlayer localPlayer) {
            if (ModClientCapabilities.FLIGHT_VIEW_CAPABILITY.get(localPlayer).resetEyeHeight()) {
                this.eyeHeight = this.eyeHeightOld + (1.0F - this.eyeHeightOld) * 0.5F;
            }
        }
    }
}
