package fuzs.fantasticwings.mixin.client;

import com.google.common.collect.Iterables;
import fuzs.fantasticwings.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(PotionContents.class)
abstract class PotionContentsMixin {

    @Inject(method = "addPotionTooltip(Ljava/lang/Iterable;Ljava/util/function/Consumer;FF)V", at = @At("HEAD"), cancellable = true)
    private static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond, CallbackInfo callback) {
        // prevent our effects from showing up on potion item tooltips, hides the implementation of the wing bottles a bit
        // also hides the fact that effects with an amplifier past 5 have no translation key
        if (Iterables.size(effects) == 1) {
            Holder<MobEffect> holder = Iterables.getOnlyElement(effects).getEffect();
            if (holder.is(ModRegistry.GROW_WINGS_MOB_EFFECT) || holder.is(ModRegistry.SHED_WINGS_MOB_EFFECT)) {
                callback.cancel();
            }
        }
    }
}
