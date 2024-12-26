package fuzs.fantasticwings.mixin.client;

import fuzs.fantasticwings.client.handler.ClientEventHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
abstract class PlayerModelMixin extends HumanoidModel<PlayerRenderState> {

    public PlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim", at = @At("TAIL"))
    public void setupAnim(PlayerRenderState renderState, CallbackInfo callback) {
        ClientEventHandler.setupPlayerAnim(renderState, this);
    }
}
